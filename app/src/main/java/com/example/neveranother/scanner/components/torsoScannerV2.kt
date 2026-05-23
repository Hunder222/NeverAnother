package com.example.neveranother.scanner.components

import android.graphics.Bitmap
import android.util.Log
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.Node
import io.github.sceneview.node.SphereNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberOnGestureListener
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.dot
import dev.romainguy.kotlin.math.normalize
import io.github.sceneview.Scene
import io.github.sceneview.collision.Vector3
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.node.CameraNode
import com.google.android.filament.Engine
import dev.romainguy.kotlin.math.length
import androidx.compose.ui.unit.TextUnit
import com.example.neveranother.viewmodels.NAViewModel
import com.google.android.filament.LightManager
import io.github.sceneview.node.LightNode
import io.github.sceneview.math.Rotation

private enum class ScannerState {
    SCANNING,
    FROZEN,
    VIEWING_3D
}

var statusTextFoundTorso = "Torso fundet"
var statusTextFindTorso = "Peg på torso"
var statusTextLockedOnto = "Låst på"
var statusTextDistance = "Afstand"
var statusTextProgress = "Fremskridt"
var statusTextFreezeNow = "Frys nu"
var statusTextHowToMeasure = "Rotér og klik for at måle"
var statusTextClearMarkers = "Fjern Markør"
var statusTextRestartScan = "Genstart Scan"
var statusTextClearScanData = "Start forfra"
var StatusTextAmountOf3DPoints = "Antal 3D punkter"

val instructionSteps = listOf(
    "1/9: Klik på toppen af BH stroppen",
    "2/9: Klik midt på venstre bryst",
    "3/9: Klik på venstre underbryst",
    "4/9: Klik på ydersiden af venstre bryst",
    "5/9: Klik midt mellem brysterne",
    "6/9: Klik på ydersiden af højre bryst",
    "7/9: Klik på højre underbryst",
    "8/9: Klik på midten af underbrystet",
    "9/9: Klik midt på højre bryst"
)

@Composable
fun TorsoScannerV2(
    naViewModel: NAViewModel,
    onScanComplete: () -> Unit
) {
    // variabler til at justere egenskaber. Til at fine-tune resultater
    val TARGET_POINT_COUNT = 1500 // Antallet af punkter der skal findes før den færdiggøre scan.
    val DEPTH_CUTOFF_METERS = 1.25f // Noder efter denne afstand fra kameraet skal ignoreres, undgår bacgrundstracking noder
    val VOXEL_SIZE = 0.015f // Node afstand for at undgå at mange noder placers på samme punkt (0.015f = 1.5 cm afstand)
    val SPHERE_RADIUS = 0.0075f // Radius på noder i 3D vieweren (0.0075f = 0.75cm diameter)


    
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
    val voxelGrid = remember { mutableSetOf<String>() }

    val cursorNode = remember(engine) {
        SphereNode(engine, radius = 0.005f).apply {
            isVisible = false
        }
    }

    var surfaceType by remember { mutableStateOf("Scanner...") }
    var isObjectLocked by remember { mutableStateOf(false) }
    var scannerState by remember { mutableStateOf(ScannerState.SCANNING) }
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
            if (capturedPointCloud.isNotEmpty()) {
                // Clear AR markers before entering 3D view to avoid coordinate confusion
                markerNodes.clear()
                markerAnchors.clear()
                scannerState = ScannerState.VIEWING_3D
            } else {
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
    }


    Box(
        modifier = Modifier.fillMaxSize().onGloballyPositioned { coords ->
            centerX = coords.size.width / 2f
            centerY = coords.size.height / 2f
            viewWidth = coords.size.width.toFloat()
            viewHeight = coords.size.height.toFloat()
        }
    ) {
        if (scannerState == ScannerState.SCANNING) {
            ARScene(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                cameraNode = cameraNode,
                childNodes = markerNodes + cursorNode,
                onViewCreated = { arSceneView = this },
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

                        val camPos = updatedFrame.camera.pose.let { Float3(it.tx(), it.ty(), it.tz()) }

                        // Accumulate points with Voxel Grid Deduplication (O(1) performance)
                        for (i in 0 until numPoints) {
                            val px = points.get(i * 4)
                            val py = points.get(i * 4 + 1)
                            val pz = points.get(i * 4 + 2)
                            val newPoint = Float3(px, py, pz)
                            
                            // Task 1: Depth Filter (Configurable)
                            val distToCam = length(newPoint - camPos)
                            if (distToCam > DEPTH_CUTOFF_METERS) continue

                            // Spatial Hashing / Voxel Grid Deduplication
                            val key = "${(px / VOXEL_SIZE).toInt()}_${(py / VOXEL_SIZE).toInt()}_${(pz / VOXEL_SIZE).toInt()}"
                            
                            if (voxelGrid.add(key)) {
                                capturedPointCloud.add(newPoint)
                            }
                        }

                        if (viewWidth > 0 && viewHeight > 0) {
                            // Project current captured cloud for overlay
                            for (point in capturedPointCloud.takeLast(600)) { 
                                val clip = FloatArray(4)
                                Matrix.multiplyMM(vpMatrix, 0, projMatrix, 0, viewMatrix, 0)
                                Matrix.multiplyMV(clip, 0, vpMatrix, 0, floatArrayOf(point.x, point.y, point.z, 1f), 0)
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

                        // Auto-freeze when target density reached
                        if (capturedPointCloud.size >= TARGET_POINT_COUNT) {
                            triggerFreeze()
                        }

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
            Canvas(modifier = Modifier.fillMaxSize()) {
                pointCloudDots.forEach { dot ->
                    drawCircle(
                        color = Color.White.copy(alpha = 0.6f), // Slightly more visible
                        radius = 2.0f,
                        center = dot
                    )
                }
            }

            // Center Reticle
            Box(modifier = Modifier.align(Alignment.Center), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(width = 24.dp, height = 1.dp).background(if (isObjectLocked) Color.Green else Color.White))
                Box(modifier = Modifier.size(width = 1.dp, height = 24.dp).background(if (isObjectLocked) Color.Green else Color.White))
            }
        }

        if (scannerState == ScannerState.VIEWING_3D) {
            PointCloudViewer(
                engine = engine,
                points = capturedPointCloud.toList(), // Pass stable snapshot
                markerNodes = markerNodes,
                sphereRadius = SPHERE_RADIUS,
                naViewModel = naViewModel,
                onScanComplete = onScanComplete,
            )
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

        // Status
        Column(modifier = Modifier.align(Alignment.TopStart).padding(top = 100.dp, start = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (scannerState == ScannerState.SCANNING) {
                StatusTag(text = if (isObjectLocked) statusTextFoundTorso else statusTextFindTorso, color = if (isObjectLocked) Color.Green else Color.Cyan)
                
                StatusTag(
                    text = "$statusTextLockedOnto : $surfaceType",
                    color = if (cursorNode.isVisible) Color.Cyan else Color.Gray
                )

                if (cursorNode.isVisible) {
                    val distMeters = sqrt(
                        (cameraNode.position.x - cursorNode.position.x).pow(2) +
                        (cameraNode.position.y - cursorNode.position.y).pow(2) +
                        (cameraNode.position.z - cursorNode.position.z).pow(2)
                    )
                    StatusTag(
                        text = "$statusTextDistance: ${"%.1f".format(distMeters * 100)} cm",
                        color = Color.White
                    )
                }

                if (isObjectLocked) {
                    StatusTag(text = "$statusTextProgress: ${capturedPointCloud.size} / $TARGET_POINT_COUNT", color = Color.Red)
                }

                if (capturedPointCloud.size > 100) {
                    Button(
                        onClick = { triggerFreeze() },
                        modifier = Modifier.padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(statusTextFreezeNow, fontSize = 12.sp)
                    }
                }

            } else if (scannerState == ScannerState.FROZEN || scannerState == ScannerState.VIEWING_3D) {
                StatusTag(text = statusTextHowToMeasure, color = Color.Cyan)
            }
        }

        if (markerNodes.isNotEmpty()) {
            Button(onClick = {
                markerNodes.clear(); markerAnchors.clear(); marker2DPoints.clear(); markerDistances.clear()
            }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp)) {
                Text(text = statusTextClearMarkers, fontFamily = NohemiFontFamily)
            }
        }

        Button(onClick = {
            markerNodes.clear(); markerAnchors.clear(); marker2DPoints.clear(); markerDistances.clear(); frozenBitmap = null
            capturedPointCloud.clear(); voxelGrid.clear(); pointCloudDots.clear(); frozenPointCloudDots.clear(); isObjectLocked = false
            if (scannerState == ScannerState.FROZEN || scannerState == ScannerState.VIEWING_3D) arSceneView?.session?.resume()
            scannerState = ScannerState.SCANNING
        }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 60.dp)) {
            Text(text = if (scannerState != ScannerState.SCANNING) statusTextRestartScan else statusTextClearScanData, fontFamily = NohemiFontFamily)
        }
    }
}

@Composable
fun PointCloudViewer(
    engine: Engine,
    points: List<Float3>,
    markerNodes: MutableList<Node>,
    sphereRadius: Float,
    naViewModel: NAViewModel,
    onScanComplete: () -> Unit
) {
    val materialLoader = io.github.sceneview.rememberMaterialLoader(engine)

    // Calculate Centroid for orbiting with NaN protection
    val centroid = remember(points) {
        if (points.isEmpty()) Position(0f, 0f, 0f)
        else {
            var sumX = 0f; var sumY = 0f; var sumZ = 0f
            var validPoints = 0
            points.forEach { p ->
                if (!p.x.isNaN() && !p.y.isNaN() && !p.z.isNaN()) {
                    sumX += p.x; sumY += p.y; sumZ += p.z
                    validPoints++
                }
            }
            if (validPoints == 0) Position(0f, 0f, 0f)
            else Position(sumX / validPoints, sumY / validPoints, sumZ / validPoints)
        }
    }

    // Build Point Cloud as a collection of SphereNodes
    val parentNode = remember(engine, points, centroid, sphereRadius) {
        val greenMaterial = materialLoader.createColorInstance(Color.Green)
        Node(engine).apply {
            points.forEach { p ->
                if (!p.x.isNaN()) {
                    addChildNode(SphereNode(engine, radius = sphereRadius).apply {
                        // Position relative to centroid
                        position = Position(p.x - centroid.x, p.y - centroid.y, p.z - centroid.z)
                        materialInstance = greenMaterial
                    })
                }
            }
        }
    }

    // Main lighting for the 3D scene
    val mainLightNode = remember(engine) {
        LightNode(engine, type = LightManager.Type.DIRECTIONAL) {
            intensity(100_000f)
        }.apply {
            rotation = Rotation(x = -45f, y = 45f, z = 0f)
        }
    }

    // Additional light from camera to ensure front-facing visibility
    val cameraLightNode = remember(engine) {
        LightNode(engine, type = LightManager.Type.POINT) {
            intensity(100_000f)
        }.apply {
            position = Position(0f, 0f, 1.5f)
        }
    }

    val cameraNode = rememberCameraNode(engine)
    val cameraManipulator = rememberCameraManipulator(
        orbitHomePosition = Position(0f, 0f, 2.0f), // Pull back further
        targetPosition = Position(0f, 0f, 0f)      // Orbit around the centroid-corrected center
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            cameraNode = cameraNode,
            cameraManipulator = cameraManipulator,
            // Use built-in gesture listener to avoid conflict with pointerInput
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, _ ->
                    Log.d("SCANNER_3D", "Tap detected at: ${motionEvent.x}, ${motionEvent.y}")

                    if (markerNodes.size < 9) {
                        // Perform 2D Screen-Space Snapping
                        val localPos = findNearestPointIn3DCloud(
                            tapX = motionEvent.x,
                            tapY = motionEvent.y,
                            points = points,
                            centroid = centroid,
                            cameraNode = cameraNode
                        )
                        
                        if (localPos != null) {
                            Log.d("SCANNER_3D", "Snapped to Point (Screen Space): $localPos")
                            val marker = SphereNode(engine, radius = 0.012f).apply {
                                this.position = localPos
                                materialInstance = materialLoader.createColorInstance(Color.Magenta)
                            }
                            markerNodes.add(marker)
                        } else {
                            Log.w("SCANNER_3D", "No point found within 150px of tap")
                        }
                    }
                }
            ),
            // Set background to opaque
            isOpaque = true,
            // Add everything to root scene to rule out parentNode issues
            childNodes = listOf(parentNode, mainLightNode, cameraLightNode) + markerNodes
        )

        // Instruction Overlay
        if (markerNodes.size < 9) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 65.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StatusTag(
                    text = instructionSteps[markerNodes.size],
                    color = Color.Cyan,
                    fontSize = 20.sp
                )
            }
        }

        // Count diagnostic
        Text(text = "$StatusTextAmountOf3DPoints: ${points.size}", color = Color.White, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp))

        // Gem mål når der markeret 9 noder
        if (markerNodes.size == 9) {
            val nodes = markerNodes.map { it.position }

            saveMeasurements(
                naViewModel,
                nodes,
                onScanComplete
            )
        }
    }
}

// udregner mål og gemmer dem i viewmodel, hvor den derefter navigere til resultatskærm der viser målene
private fun saveMeasurements(
    naViewModel: NAViewModel,
    nodes: List<Position>,
    onScanComplete: () -> Unit
){
    // Breast Height: Strap(0), Left Apex(1), Left Underbust(2)
    val breastHeight = calculateSurfaceDistance(nodes[0], nodes[1], nodes[2])

    // Breast Span: Left Outer(3), Left Apex(1), Center Gore(4)
    val breastSpan = calculateSurfaceDistance(nodes[3], nodes[1], nodes[4])

    // Upper Circ: Right Outer(5), Left Outer(3), Right Apex(8), Left Apex(1)
    val upperCirc = calculateEllipseCircumference(nodes[5], nodes[3], nodes[8], nodes[1])

    // Lower Circ: Right Underbust(6), Left Underbust(2), Center Underbust(7), null
    val lowerCirc = calculateEllipseCircumference(nodes[6], nodes[2], nodes[7], null)

    // naViewModel measurements og save
    naViewModel.chestHeight = breastHeight.toString()
    naViewModel.chestWidth = breastSpan.toString()
    naViewModel.upperCircumference = upperCirc.toString()
    naViewModel.lowerCircumference = lowerCirc.toString()

    naViewModel.saveCurrentMeasurements()

    // naviger til resultatskærm
    onScanComplete()
}




private fun findNearestPointIn3DCloud(
    tapX: Float,
    tapY: Float,
    points: List<Float3>,
    centroid: Position,
    cameraNode: CameraNode
): Position? {
    var minDistance = Float.MAX_VALUE
    var bestLocalPoint: Position? = null
    val snapThreshold = 150f // 150 pixel tap radius

    for (p in points) {
        // Correct for the centroid offset used in rendering
        val localP = Position(p.x - centroid.x, p.y - centroid.y, p.z - centroid.z)
        
        // Project the 3D world/local point into 2D screen coordinates
        // worldToScreenPoint expects Vector3, so we convert localP (Float3/Position)
        val screenPoint = cameraNode.worldToScreenPoint(Vector3(localP.x, localP.y, localP.z))
        
        // Skip points that are behind the camera (returns NaN)
        if (screenPoint.x.isNaN()) continue
        
        // Calculate 2D pixel distance between tap and projected point
        val dx = tapX - screenPoint.x
        val dy = tapY - screenPoint.y
        val dist = sqrt(dx * dx + dy * dy)

        // Snapping logic: find the absolute closest point within the threshold
        if (dist < snapThreshold && dist < minDistance) {
            minDistance = dist
            bestLocalPoint = localP
        }
    }
    
    return bestLocalPoint
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



//
// Funktioner til at lave målinger ud fra de 9 punkter
//

// Til bryst højde og brystbredde, finder afstand mellem punkter, og udregner derefter en curve mellem 3 punkter fora t simulere målebåndet
private fun calculateDistanceInCm(p1: Position, p2: Position): Float = sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2) + (p2.z - p1.z).pow(2)) * 100f
private fun calculateSurfaceDistance(s: Position, a: Position, e: Position): Float = calculateDistanceInCm(s, a) + calculateDistanceInCm(a, e)

// Til øvre og under omkreds, ved at kende den halve torso kan vi bruge Ramanujan formel til at udregne resten af omkredsen
private fun calculateEllipseCircumference(
    pOuter1: Position,
    pOuter2: Position,
    pFront1: Position,
    pFront2: Position? = null
): Float {
    // semi-major axis a: 3D distance between pOuter1 and pOuter2 / 2
    val a = sqrt(
        (pOuter2.x - pOuter1.x).pow(2) +
                (pOuter2.y - pOuter1.y).pow(2) +
                (pOuter2.z - pOuter1.z).pow(2)
    ) / 2f

    // semi-minor axis b: absolute difference between outer Z average and front Z average
    val avgZOuter = (pOuter1.z + pOuter2.z) / 2f
    val avgZFront = if (pFront2 == null) pFront1.z else (pFront1.z + pFront2.z) / 2f
    val b = abs(avgZOuter - avgZFront)

    // Ramanujan formula: PI * (3*(a+b) - sqrt((3*a+b)*(a+3*b)))
    val term1 = 3f * (a + b)
    val term2 = sqrt((3f * a + b) * (a + 3f * b))
    val circumferenceMeters = Math.PI.toFloat() * (term1 - term2)

    return circumferenceMeters * 100f
}

// Composable til at lave status text under scanning
@Composable
private fun StatusTag(text: String, color: Color, fontSize: TextUnit = 12.sp) {
    Box(modifier = Modifier.background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(text = text, color = color, fontSize = fontSize, fontWeight = FontWeight.Bold, fontFamily = NohemiFontFamily)
    }
}
