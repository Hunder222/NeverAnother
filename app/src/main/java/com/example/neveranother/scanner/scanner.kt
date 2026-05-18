package com.example.neveranother.scanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.neveranother.ui.theme.NohemiFontFamily

@Composable
fun Scanner(){
    Text(
        fontFamily = NohemiFontFamily,
        text = "This is scanner",
        fontSize = 30.sp,
    )
}