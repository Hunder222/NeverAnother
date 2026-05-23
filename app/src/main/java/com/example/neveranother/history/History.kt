package com.example.neveranother.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.neveranother.viewmodels.Measurement
import com.example.neveranother.viewmodels.NAViewModel

@Composable
fun History(
    viewModel: NAViewModel,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Text(
            text = "Målehistorik",
            fontFamily = NohemiFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = NAaccentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.savedMeasurements.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Du har ingen gemte målinger endnu.",
                    fontSize = 18.sp,
                    color = NAtextBlack.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(viewModel.savedMeasurements.asReversed()) { measurement ->
                    HistoryCard(measurement)
                }
            }
        }

        // Back Button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 12.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NAwarmGrey),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Tilbage", color = NAtextBlack, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HistoryCard(measurement: Measurement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = NAwarmGrey.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Måling",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NAtextBlack
                )
                Text(
                    text = measurement.date,
                    fontSize = 14.sp,
                    color = NAtextBlack.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MeasurementRow("Øvre Omkreds", "${measurement.upperCircumference} cm")
                MeasurementRow("Nedre Omkreds", "${measurement.lowerCircumference} cm")
                MeasurementRow("Volume", measurement.chestVolume)
                MeasurementRow("Brystbredde", "${measurement.chestWidth} cm")
                MeasurementRow("Brysthøjde", "${measurement.chestHeight} cm")
            }
        }
    }
}

@Composable
fun MeasurementRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp, color = NAtextBlack.copy(alpha = 0.8f))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = NAtextBlack)
    }
}
