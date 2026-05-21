package com.example.neveranother.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.neveranother.ui.theme.Inter
import com.example.neveranother.ui.theme.NAboxColor
import com.example.neveranother.ui.theme.NAtextBlack
import com.example.neveranother.ui.theme.NohemiFontFamily

// Reusable text composables

@Composable
fun NAHeader1(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 30.sp
) {
    Text(
        text = text,
        fontFamily = NohemiFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = fontSize,
        color = NAtextBlack,
        modifier = modifier
    )
}

@Composable
fun NAHeader2(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = NohemiFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        color = NAtextBlack,
        modifier = modifier
    )
}

@Composable
fun NABodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NAtextBlack,
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        fontFamily = Inter,
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}

@Composable
fun NAMicroCopy(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = NAboxColor,
        modifier = modifier
    )
}

