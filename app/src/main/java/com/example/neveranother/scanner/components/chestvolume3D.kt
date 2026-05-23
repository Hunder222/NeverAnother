package com.example.neveranother.scanner.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.R
import com.example.neveranother.manual.ManualNavigationButtons
import com.example.neveranother.ui.theme.NAaccentColor
import com.example.neveranother.ui.theme.NAtextBlack
import com.example.neveranother.ui.theme.NAwarmGrey
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.example.neveranother.viewmodels.NAViewModel

@Composable
fun ChestVolume3D(
    viewModel: NAViewModel,
    onBack: () -> Unit,
    onStartScanClick: () -> Unit
) {
    val options = listOf(
        "Firmer Top Volume",
        "Softer Top Volume",
        "Firmer Bottom Volume",
        "Softer Bottom Volume"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Bryst Volume",
            fontFamily = NohemiFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = NAtextBlack,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Subtitle
            Text(
                text = "Hvilken Type Fylde Kommer Tættest På Din?",
                fontFamily = NohemiFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                color = NAtextBlack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Description
            Text(
                text = "Hvis du har mest fylde øverst på brystet, er du en “topfylde”. Hvis din fylde primært sidder nederst på brystet, er du en “bundfylde”. Tjek nu, om din fylde er mere fast eller blød, ved at finde det billede, der ligner dig mest.",
                fontSize = 14.sp,
                color = NAtextBlack,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Options
            options.forEach { option ->
                val isSelected = viewModel.chestVolume == option
                val imageRes = when (option) {
                    "Firmer Top Volume" -> R.drawable.firmer_top_volumen
                    "Softer Top Volume" -> R.drawable.softer_top_volumen
                    "Firmer Bottom Volume" -> R.drawable.firmer_bottom_volumen
                    "Softer Bottom Volume" -> R.drawable.softer_bottom_volumen
                    else -> R.drawable.threed
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NAwarmGrey)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) NAaccentColor else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { viewModel.chestVolume = option }
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = option,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            text = option,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NAtextBlack
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        NavigationButtons3D(
            onBack = onBack,
            onNext = onStartScanClick,
            nextEnabled = viewModel.chestVolume.isNotEmpty()
        )
    }
}


@Composable
fun NavigationButtons3D(
    onBack: () -> Unit,
    onNext: () -> Unit,
    nextEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Tilbage", color = NAtextBlack, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onNext,
            enabled = nextEnabled,
            modifier = Modifier
                .weight(1.5f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NAaccentColor,
                disabledContainerColor = NAaccentColor.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Start Scan", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}