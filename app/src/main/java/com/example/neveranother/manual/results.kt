package com.example.neveranother.manual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.ui.theme.*
import com.example.neveranother.viewmodels.NAViewModel

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle

@Composable
fun Results(
    naViewModel: NAViewModel,
    onBack: () -> Unit = {},
    onGoToCart: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Title
            Text(
                text = "Dine Resultater",
                fontFamily = NohemiFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = NAaccentDowntoned,
                textAlign = TextAlign.Center,
                lineHeight = 52.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Subtitle
            Text(
                text = "Baseret på dine mål har vi et bh-design, der passer til din krop.",
                fontSize = 16.sp,
                color = NAtextBlack,
                textAlign = TextAlign.Start,
                lineHeight = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Result Cards
            ResultCard(title = "Øvre Omkreds", value = "${naViewModel.upperCircumference} cm")
            ResultCard(title = "Nedre Omkreds", value = "${naViewModel.lowerCircumference} cm")
            ResultCard(title = "Volume", value = naViewModel.chestVolume)
            ResultCard(title = "Brystbredde", value = "${naViewModel.chestWidth} cm")
            ResultCard(title = "Brysthøjde", value = "${naViewModel.chestHeight} cm")

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Bottom Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Tilbage", color = NAtextBlack, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onGoToCart,
                modifier = Modifier
                    .weight(1.5f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NAaccentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Færdig", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ResultCard(title: String, value: String) {
    val displayValue = buildAnnotatedString {
        if (value.endsWith(" cm")) {
            append(value.substringBefore(" cm"))
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append(" cm")
            }
        } else {
            append(value)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(NAwarmGrey.copy(alpha = 0.4f))
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontFamily = NohemiFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                color = NAtextBlack
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(NAwarmGrey)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayValue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NAtextBlack
                )
            }
        }
    }
}
