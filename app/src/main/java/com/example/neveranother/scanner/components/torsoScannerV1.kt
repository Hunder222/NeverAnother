package com.example.neveranother.scanner.components

import android.R.attr.text
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.Point
import com.google.ar.core.InstantPlacementPoint
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.HitResult
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.node.SphereNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.math.Position
import io.github.sceneview.node.Node
import kotlin.math.pow
import kotlin.math.sqrt


@Composable
fun TorsoScannerV1() {
    val engine = rememberEngine()
    val cameraNode = rememberARCameraNode(engine = engine)
    var arFrame by remember { mutableStateOf<Frame?>(null) }

    var centerX by remember { mutableStateOf(0f) }
    var centerY by remember { mutableStateOf(0f) }

    val placedAnchors = remember { mutableStateListOf<Anchor>() }
    val markerNodes = remember { mutableStateListOf<Node>() }

    val cursorNode = remember(engine) {
        SphereNode(engine, radius = 0.005f).apply {
            isVisible = false
        }
    }

    var surfaceType by remember { mutableStateOf("Scanning...") }

    fun findBestHit(results: List<HitResult>): HitResult? {
        return results.firstOrNull { hit ->
            when (val trackable = hit.trackable) {
                is Plane -> trackable.isPoseInPolygon(hit.hitPose)
                is Point -> trackable.orientationMode == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL
                is InstantPlacementPoint -> true
                else -> false
            }
        } ?: results.firstOrNull()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                centerX = coords.size.width / 2f
                centerY = coords.size.height / 2f
            }
    ) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            cameraNode = cameraNode,
            childNodes = remember(markerNodes.size) { markerNodes + cursorNode },
            sessionConfiguration = { session, config ->
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    config.depthMode = Config.DepthMode.AUTOMATIC
                }
                config.focusMode = Config.FocusMode.AUTO
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            onSessionUpdated = { _, updatedFrame ->
                arFrame = updatedFrame

                if (centerX > 0f && centerY > 0f) {
                    val hitResults = updatedFrame.hitTest(centerX, centerY)
                    val hitResult = findBestHit(hitResults)

                    if (hitResult != null) {
                        cursorNode.isVisible = true
                        cursorNode.position = Position(
                            x = hitResult.hitPose.tx(),
                            y = hitResult.hitPose.ty(),
                            z = hitResult.hitPose.tz()
                        )

                        surfaceType = when (val t = hitResult.trackable) {
                            is Plane -> {
                                if (t.type == Plane.Type.VERTICAL) "Vertical Plane"
                                else "Horizontal Plane"
                            }
                            is Point -> "Surface Feature"
                            is InstantPlacementPoint -> "Instant Surface"
                            else -> "Detected Object"
                        }
                    } else {
                        cursorNode.isVisible = false
                        surfaceType = "No Surface"
                    }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, _ ->
                    val hitResults = arFrame?.hitTest(motionEvent.x, motionEvent.y) ?: emptyList()
                    val hitResult = findBestHit(hitResults)

                    hitResult?.let { hit ->
                        val newAnchor = hit.createAnchor()
                        placedAnchors.add(newAnchor)

                        val visualMarker = SphereNode(
                            engine = engine,
                            radius = 0.012f
                        ).apply {
                            position = Position(
                                x = hit.hitPose.tx(),
                                y = hit.hitPose.ty(),
                                z = hit.hitPose.tz()
                            )
                        }
                        markerNodes.add(visualMarker)
                    }
                }
            )
        )

        // Center Reticle
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 24.dp, height = 1.dp)
                    .background(Color.White.copy(alpha = 0.8f))
            )
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 24.dp)
                    .background(Color.White.copy(alpha = 0.8f))
            )
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(Color.White, shape = CircleShape)
            )
        }

        // Status Indicators
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 100.dp, start = 16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            val trackingState = arFrame?.camera?.trackingState
            val isReady = trackingState == TrackingState.TRACKING

            StatusTag(
                text = if (isReady) "AR READY" else "SEARCHING SCALE...",
                color = if (isReady) Color.Green else Color.Yellow
            )

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
        }

        // Measurement Logic
        if (markerNodes.size == 3) {
            val totalSurfaceCm = calculateSurfaceDistance(
                markerNodes[0].position,
                markerNodes[1].position,
                markerNodes[2].position
            )

            Text(
                text = "Surface Measurement: ${"%.2f".format(totalSurfaceCm)} cm",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = NohemiFontFamily,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
            )

        } else if (markerNodes.size in 1..2) {
            Text(
                text = "Place ${3 - markerNodes.size} more markers along the curve...",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
            )
        }

        Button(
            onClick = {
                markerNodes.clear()
                placedAnchors.clear()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Text(text = "Clear Markers", fontFamily = NohemiFontFamily)
        }
    }
}

private fun calculateDistanceInCm(p1: Position, p2: Position): Float {
    return sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2) + (p2.z - p1.z).pow(2)) * 100f
}

private fun calculateSurfaceDistance(start: Position, apex: Position, end: Position): Float {
    return calculateDistanceInCm(start, apex) + calculateDistanceInCm(apex, end)
}

@Composable
private fun StatusTag(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = NohemiFontFamily
        )
    }
}