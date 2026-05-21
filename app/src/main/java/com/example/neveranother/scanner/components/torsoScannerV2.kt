package com.example.neveranother.scanner.components

import android.graphics.Bitmap
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.HitResult
import com.google.ar.core.InstantPlacementPoint
import com.google.ar.core.Plane
import com.google.ar.core.Point
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.Node
import io.github.sceneview.node.SphereNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberOnGestureListener
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.dot
import dev.romainguy.kotlin.math.normalize

private enum class ScannerState {
    SCANNING,
    FROZEN
}

@Composable
fun TorsoScannerV2() {
    val freezeTimerSeconds = 10 // EDIT THIS to change the countdown duration
    
    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine = engine)
    var arFrame by remember { mutableStateOf<Frame?>(null) }
    var arSceneView by remember { mutableStateOf<ARSceneView?>(null) }

    var centerX by remember { mutableStateOf(0f) }
    var centerY by remember { mutableStateOf(0f) }

    val markerNodes = remember { mutableStateListOf<Node>() }
    val markerAnchors = remember { mutableStateListOf<Anchor>() }
    val marker2DPoints = remember { mutableStateListOf<Offset>() }
    val markerDistances = remember { mutableStateListOf<Float>() } // Store distances for scaling
    val pointCloudDots = remember { mutableStateListOf<Offset>() }
    val frozenPointCloudDots = remember { mutableStateListOf<Offset>() }
    val capturedPointCloud = remember { mutableStateListOf<Float3>() }

    val cursorNode = remember(engine) {
        SphereNode(engine, radius = 0.005f).apply {
            isVisible = false
        }
    }

    var surfaceType by remember { mutableStateOf("Scanning...") }
    var isObjectLocked by remember { mutableStateOf(false) }
    var scannerState by remember { mutableStateOf(ScannerState.SCANNING) }
    var countdown by remember { mutableStateOf(freezeTimerSeconds) }
    var frozenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Geometric HitTest State
    var frozenViewMatrix by remember { mutableStateOf<FloatArray?>(null) }
    var frozenProjMatrix by remember { mutableStateOf<FloatArray?>(null) }
    var viewWidth by remember { mutableStateOf(0f) }
    var viewHeight by remember { mutableStateOf(0f) }

    // Define helper functions at the top to avoid scope issues
    fun findBestHit(results: List<HitResult>): HitResult? {
        val pointHits = results.filter { hit ->
            val trackable = hit.trackable
            trackable is Point && trackable.orientationMode == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL
        }
        if (pointHits.isNotEmpty()) return pointHits.first()

        val verticalPlanes = results.filter { hit ->
            val trackable = hit.trackable
            trackable is Plane && trackable.type == Plane.Type.VERTICAL
        }
        if (verticalPlanes.isNotEmpty()) return verticalPlanes.first()

        val horizontalPlanes = results.filter { hit ->
            val trackable = hit.trackable
            trackable is Plane && trackable.type == Plane.Type.HORIZONTAL_UPWARD_FACING
        }
        if (horizontalPlanes.isNotEmpty()) return horizontalPlanes.first()

        return results.firstOrNull()
    }

    fun triggerFreeze() {
        if (scannerState != ScannerState.SCANNING) return
        
        arSceneView?.let { view ->
            // Capture the current point cloud for snapping
            arFrame?.acquirePointCloud()?.let { pc ->
                val points = pc.points
                val numPoints = points.remaining() / 4
                capturedPointCloud.clear()
                for (i in 0 until numPoints) {
                    capturedPointCloud.add(Float3(points.get(i * 4), points.get(i * 4 + 1), points.get(i * 4 + 2)))
                }
                pc.release()
            }

            arFrame?.let { frame ->
                val projMatrix = FloatArray(16)
                frame.camera.getProjectionMatrix(projMatrix, 0, 0.1f, 100.0f)
                val viewM = FloatArray(16)
                frame.camera.getViewMatrix(viewM, 0)
                frozenProjMatrix = projMatrix
                frozenViewMatrix = viewM
            }
            viewWidth = view.width.toFloat()
            viewHeight = view.height.toFloat()

            captureARBitmap(view) { bitmap ->
                frozenBitmap = bitmap
                
                // Project captured points into screen space for frozen visualization
                arFrame?.let { frame ->
                    val projM = FloatArray(16)
                    val viewM = FloatArray(16)
                    frame.camera.getProjectionMatrix(projM, 0, 0.1f, 100.0f)
                    frame.camera.getViewMatrix(viewM, 0)
                    val vpMatrix = FloatArray(16)
                    Matrix.multiplyMM(vpMatrix, 0, projM, 0, viewM, 0)
                    
                    val projected = mutableListOf<Offset>()
                    for (point in capturedPointCloud) {
                        val clip = FloatArray(4)
                        Matrix.multiplyMV(clip, 0, vpMatrix, 0, floatArrayOf(point.x, point.y, point.z, 1f), 0)
                        if (clip[3] > 0) {
                            val ndcX = clip[0] / clip[3]
                            val ndcY = clip[1] / clip[3]
                            projected.add(Offset((ndcX + 1f) / 2f * viewWidth, (1f - ndcY) / 2f * viewHeight))
                        }
                    }
                    frozenPointCloudDots.clear()
                    frozenPointCloudDots.addAll(projected)
                }
                
                scannerState = ScannerState.FROZEN
            }
        }
    }

    LaunchedEffect(scannerState, arFrame?.camera?.trackingState, isObjectLocked) {
        if (scannerState == ScannerState.SCANNING) {
            if (arFrame?.camera?.trackingState == TrackingState.TRACKING && isObjectLocked) {
                while (countdown > 0) {
                    delay(1000)
                    if (arFrame?.camera?.trackingState != TrackingState.TRACKING || !isObjectLocked) {
                        countdown = freezeTimerSeconds
                        break
                    }
                    countdown--
                }
                if (countdown == 0 && markerAnchors.isEmpty()) {
                    triggerFreeze()
                }
            } else { countdown = freezeTimerSeconds }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().onGloballyPositioned { coords ->
            centerX = coords.size.width / 2f
            centerY = coords.size.height / 2f
        }
    ) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            cameraNode = cameraNode,
            childNodes = markerNodes + cursorNode,
            onViewCreated = { arSceneView = this },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, _ ->
                    if (scannerState == ScannerState.SCANNING) {
                        val hitResults = arFrame?.hitTest(motionEvent.x, motionEvent.y) ?: emptyList()
                        val hitResult = findBestHit(hitResults)

                        hitResult?.let { hit ->
                            if (markerAnchors.size < 3) {
                                val anchor = hit.createAnchor()
                                markerAnchors.add(anchor)
                                markerNodes.add(SphereNode(engine, radius = 0.012f).apply {
                                    position = Position(hit.hitPose.tx(), hit.hitPose.ty(), hit.hitPose.tz())
                                })
                            }
                        }
                    }
                }
            ),
            sessionConfiguration = { session, config ->
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    config.depthMode = Config.DepthMode.AUTOMATIC
                }
                config.focusMode = Config.FocusMode.AUTO
            },
            onSessionUpdated = { _, updatedFrame ->
                arFrame = updatedFrame
                if (scannerState == ScannerState.SCANNING) {
                    // Update Point Cloud Dots for visual confirmation
                    val pointCloud = updatedFrame.acquirePointCloud()
                    val points = pointCloud.points
                    val numPoints = points.remaining() / 4
                    val newDots = mutableListOf<Offset>()
                    
                    val projMatrix = FloatArray(16)
                    updatedFrame.camera.getProjectionMatrix(projMatrix, 0, 0.1f, 100.0f)
                    val viewMatrix = FloatArray(16)
                    updatedFrame.camera.getViewMatrix(viewMatrix, 0)
                    val vpMatrix = FloatArray(16)
                    Matrix.multiplyMM(vpMatrix, 0, projMatrix, 0, viewMatrix, 0)

                    if (viewWidth > 0 && viewHeight > 0) {
                        for (i in 0 until numPoints.coerceAtMost(600)) { // Increased density
                            val x = points.get(i * 4)
                            val y = points.get(i * 4 + 1)
                            val z = points.get(i * 4 + 2)
                            val clip = FloatArray(4)
                            Matrix.multiplyMV(clip, 0, vpMatrix, 0, floatArrayOf(x, y, z, 1f), 0)
                            if (clip[3] > 0) {
                                val ndcX = clip[0] / clip[3]
                                val ndcY = clip[1] / clip[3]
                                newDots.add(Offset((ndcX + 1f) / 2f * viewWidth, (1f - ndcY) / 2f * viewHeight))
                            }
                        }
                    }
                    pointCloudDots.clear()
                    pointCloudDots.addAll(newDots)
                    pointCloud.release()

                    if (centerX > 0f && centerY > 0f) {
                        val hitResults = updatedFrame.hitTest(centerX, centerY)
                        val hitResult = findBestHit(hitResults)
                        if (hitResult != null) {
                            cursorNode.isVisible = true
                            cursorNode.position = Position(hitResult.hitPose.tx(), hitResult.hitPose.ty(), hitResult.hitPose.tz())
                            val trackable = hitResult.trackable
                            surfaceType = when (trackable) {
                                is Plane -> {
                                    if (trackable.type == Plane.Type.VERTICAL) "Vertical Plane"
                                    else "Horizontal Plane"
                                }
                                is Point -> "Surface Feature"
                                is InstantPlacementPoint -> "Instant Surface"
                                else -> "Detected Object"
                            }
                            
                            // Visual indication of object vs floor
                            isObjectLocked = when (trackable) {
                                is Plane -> trackable.type == Plane.Type.VERTICAL
                                is Point -> trackable.orientationMode == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL
                                else -> true 
                            }
                        } else {
                            cursorNode.isVisible = false
                            isObjectLocked = false
                        }
                    }
                }
            }
        )

        // Point Cloud Overlay for visual shape confirmation
        if (scannerState == ScannerState.SCANNING) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                pointCloudDots.forEach { dot ->
                    drawCircle(
                        color = Color.White.copy(alpha = 0.6f), // Slightly more visible
                        radius = 2.0f,
                        center = dot
                    )
                }
            }
        }

        if (scannerState == ScannerState.FROZEN && frozenBitmap != null) {
            Box(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (markerAnchors.size < 3) {
                        val worldPos = findNearestPointCloudPoint(
                            offset.x, offset.y, viewWidth, viewHeight,
                            frozenProjMatrix!!, frozenViewMatrix!!, capturedPointCloud
                        )
                        
                        worldPos?.let { snappedPos ->
                            arSceneView?.session?.let { session ->
                                val anchor = session.createAnchor(Pose(floatArrayOf(snappedPos.x, snappedPos.y, snappedPos.z), floatArrayOf(0f, 0f, 0f, 1f)))
                                markerAnchors.add(anchor)
                                markerNodes.add(SphereNode(engine, radius = 0.012f).apply { position = snappedPos })
                                marker2DPoints.add(offset)
                                
                                // Calculate distance to camera for scaling
                                val camPos = cameraNode.worldPosition
                                val dist = sqrt((snappedPos.x - camPos.x).pow(2) + (snappedPos.y - camPos.y).pow(2) + (snappedPos.z - camPos.z).pow(2))
                                markerDistances.add(dist)
                            }
                        }
                    }
                }
            }) {
                Image(bitmap = frozenBitmap!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Show the captured mapping dots so the user knows where they can tap
                    frozenPointCloudDots.forEach { dot ->
                        drawCircle(
                            color = Color.White.copy(alpha = 0.3f),
                            radius = 1.5f,
                            center = dot
                        )
                    }

                    marker2DPoints.forEachIndexed { index, point ->
                        val dist = markerDistances.getOrNull(index) ?: 0.5f
                        // Scale radius based on distance: closer = larger, further = smaller
                        // Base size 15f at 0.5m, scales inversely with distance
                        val radius = (15f * (0.5f / dist)).coerceIn(5f, 30f)
                        drawCircle(color = Color.Cyan, radius = radius, center = point)
                    }
                }
            }
        }

        // Center Reticle
        if (scannerState == ScannerState.SCANNING) {
            Box(modifier = Modifier.align(Alignment.Center), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(width = 24.dp, height = 1.dp).background(if (isObjectLocked) Color.Green else Color.White))
                Box(modifier = Modifier.size(width = 1.dp, height = 24.dp).background(if (isObjectLocked) Color.Green else Color.White))
            }
        }

        // Status
        Column(modifier = Modifier.align(Alignment.TopStart).padding(top = 100.dp, start = 16.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            if (scannerState == ScannerState.SCANNING) {
                StatusTag(text = if (isObjectLocked) "OBJECT LOCKED" else "AIM AT OBJECT", color = if (isObjectLocked) Color.Green else Color.Cyan)
                
                StatusTag(
                    text = "LOCKED: $surfaceType",
                    color = if (cursorNode.isVisible) Color.Cyan else Color.Gray
                )

                if (cursorNode.isVisible) {
                    val distMeters = sqrt(
                        (cameraNode.position.x - cursorNode.position.x).pow(2) +
                        (cameraNode.position.y - cursorNode.position.y).pow(2) +
                        (cameraNode.position.z - cursorNode.position.z).pow(2)
                    )
                    StatusTag(
                        text = "DEPTH: ${"%.1f".format(distMeters * 100)} cm",
                        color = Color.White
                    )
                }

                if (isObjectLocked) {
                    StatusTag(text = "FREEZING IN: $countdown s", color = Color.Red)
                    
                    Button(
                        onClick = { triggerFreeze() },
                        modifier = Modifier.padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Freeze Now", fontSize = 12.sp)
                    }
                }
            } else if (scannerState == ScannerState.FROZEN) {
                StatusTag(text = "FROZEN - TAP TO MEASURE", color = Color.Cyan)
            }
        }

        if (markerNodes.size == 3) {
            Text(text = "Measurement: ${"%.2f".format(calculateSurfaceDistance(markerNodes[0].position, markerNodes[1].position, markerNodes[2].position))} cm",
                color = Color.White, fontSize = 24.sp, fontFamily = NohemiFontFamily, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp))
        }

        Button(onClick = {
            markerNodes.clear(); markerAnchors.clear(); marker2DPoints.clear(); markerDistances.clear(); frozenBitmap = null
            capturedPointCloud.clear(); frozenPointCloudDots.clear(); countdown = freezeTimerSeconds; isObjectLocked = false
            if (scannerState == ScannerState.FROZEN) arSceneView?.session?.resume()
            scannerState = ScannerState.SCANNING
        }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 60.dp)) {
            Text(text = if (scannerState == ScannerState.FROZEN) "Restart Scan" else "Clear Markers", fontFamily = NohemiFontFamily)
        }
    }
}

private fun findNearestPointCloudPoint(
    x: Float, y: Float, width: Float, height: Float,
    projMatrix: FloatArray, viewMatrix: FloatArray,
    pointCloud: List<Float3>
): Position? {
    if (pointCloud.isEmpty()) return null

    val invVP = FloatArray(16)
    Matrix.multiplyMM(invVP, 0, projMatrix, 0, viewMatrix, 0)
    Matrix.invertM(invVP, 0, invVP, 0)

    val ndcX = (2.0f * x / width) - 1.0f
    val ndcY = 1.0f - (2.0f * y / height)

    val nearResult = FloatArray(4)
    val farResult = FloatArray(4)
    Matrix.multiplyMV(nearResult, 0, invVP, 0, floatArrayOf(ndcX, ndcY, -1f, 1f), 0)
    Matrix.multiplyMV(farResult, 0, invVP, 0, floatArrayOf(ndcX, ndcY, 1f, 1f), 0)

    val rayOrigin = Float3(nearResult[0] / nearResult[3], nearResult[1] / nearResult[3], nearResult[2] / nearResult[3])
    val rayDir = normalize(Float3(farResult[0] / farResult[3], farResult[1] / farResult[3], farResult[2] / farResult[3]) - rayOrigin)

    val hitCluster = mutableListOf<Float3>()
    val snapRadius = 0.035f // Relaxed radius to 3.5cm for better testdoll detection
    var absoluteNearestPoint: Float3? = null
    var minDistanceToRay = Float.MAX_VALUE

    for (point in pointCloud) {
        val v = point - rayOrigin
        val projectionLength = dot(v, rayDir)
        
        // Only look at points that are physically IN FRONT of the camera
        if (projectionLength > 0) { 
            val closestPointOnRay = rayOrigin + rayDir * projectionLength
            val dist = dev.romainguy.kotlin.math.length(point - closestPointOnRay)

            // Track absolute nearest for fallback
            if (dist < minDistanceToRay) {
                minDistanceToRay = dist
                absoluteNearestPoint = point
            }

            // If the point is inside our snap cylinder, add to cluster
            if (dist <= snapRadius) {
                hitCluster.add(point)
            }
        }
    }

    // Phase 1: Try averaging the cluster for precision
    if (hitCluster.isNotEmpty()) {
        var sumX = 0f; var sumY = 0f; var sumZ = 0f
        for (p in hitCluster) { sumX += p.x; sumY += p.y; sumZ += p.z }
        val count = hitCluster.size.toFloat()
        return Position(sumX / count, sumY / count, sumZ / count)
    } 
    
    // Phase 2: Fallback to absolute nearest point if within reasonable range (e.g., 10cm)
    if (minDistanceToRay < 0.10f) {
        return absoluteNearestPoint?.let { Position(it.x, it.y, it.z) }
    }

    return null
}

private fun captureARBitmap(view: ARSceneView, onBitmapCaptured: (Bitmap?) -> Unit) {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    PixelCopy.request(view, bitmap, { result ->
        if (result == PixelCopy.SUCCESS) onBitmapCaptured(bitmap)
        else onBitmapCaptured(null)
    }, Handler(Looper.getMainLooper()))
}

/* [DELETE] performFakeHitTest */

private fun calculateDistanceInCm(p1: Position, p2: Position): Float = sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2) + (p2.z - p1.z).pow(2)) * 100f
private fun calculateSurfaceDistance(s: Position, a: Position, e: Position): Float = calculateDistanceInCm(s, a) + calculateDistanceInCm(a, e)

@Composable
private fun StatusTag(text: String, color: Color) {
    Box(modifier = Modifier.background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(text = text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = NohemiFontFamily)
    }
}
