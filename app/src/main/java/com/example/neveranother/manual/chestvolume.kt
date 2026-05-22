package com.example.neveranother.manual

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.R
import com.example.neveranother.ui.theme.NAaccentColor
import com.example.neveranother.ui.theme.NAtextBlack
import com.example.neveranother.ui.theme.NAwarmGrey
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.example.neveranother.viewmodels.NAViewModel

@Composable
fun ChestVolume(
    viewModel: NAViewModel,
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
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
        ManualProgressBar(currentStepIndex = 2)

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
                    "Firmer Top Volume" -> R.drawable.top_volume_firmer
                    "Softer Top Volume" -> R.drawable.top_volume_softer
                    "Firmer Bottom Volume" -> R.drawable.bottom_volume_firmer
                    "Softer Bottom Volume" -> R.drawable.bottom_volume_softer
                    else -> R.drawable.top_volume_firmer
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.5f)),
                            contentScale = ContentScale.Fit
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

        ManualNavigationButtons(
            onBack = onBack,
            onNext = onNext,
            nextEnabled = viewModel.chestVolume.isNotEmpty()
        )
    }
}
