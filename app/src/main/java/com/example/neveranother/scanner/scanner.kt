package com.example.neveranother.scanner

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.neveranother.scanner.components.TorsoScannerV1
import com.example.neveranother.scanner.components.TorsoScannerV2
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.example.neveranother.viewmodels.NAViewModel


@Composable
fun Scanner(
    NAviewmodel: NAViewModel,
    onScanComplete: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TorsoScannerV2(
            NAviewmodel,
            onScanComplete
        )
    }
}

