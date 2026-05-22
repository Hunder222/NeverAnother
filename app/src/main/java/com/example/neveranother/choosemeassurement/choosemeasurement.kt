package com.example.neveranother.choosemeassurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neveranother.components.NABodyText
import com.example.neveranother.components.NAHeader1
import com.example.neveranother.ui.theme.NAaccentColor
import com.example.neveranother.ui.theme.NAbackgroundColor

@Composable
fun Choosemeasurement(
    onManualClick: () -> Unit,
    onScannerClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NAHeader1("Vælg målemetode")
            Spacer(modifier = Modifier.height(8.dp))
            NABodyText("Vælg hvordan du vil foretage dine mål")

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onScannerClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NAaccentColor)
            ) {
                Text("3D Scanner (Anbefalet)", color = NAbackgroundColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onManualClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NAaccentColor.copy(alpha = 0.1f))
            ) {
                Text("Manuel indtastning", color = NAaccentColor)
            }
        }
    }
}
