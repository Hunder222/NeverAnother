package com.example.neveranother.choosemeassurement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.neveranother.R
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

//these are the texts for the page in differnt languages
val btn3DText: String = "3D-scan"
var btnManualDanish: String = "Manuel"
var btnManualEnglish: String = "Manual"

@Composable
fun Choosemeasurement(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MeassurementCard()
    }
}
@Composable
fun MeassurementCard() { // use same for both so that changes can be made for both in one place

    var showInfo by remember { // remembers wheter or not info should be shown
        mutableStateOf(false) // does so that info is automatically not shown
    }

    Card( // makes it easy to reuse
        shape = RoundedCornerShape(15.dp)
    ) {
        Box { // box used so things can be placed on top of each other
            Image(
                painter = painterResource(id = R.drawable.threed), // TODO: at gøre til parameter
                contentDescription = "", // TODO: at gøre til parameter
                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
            )

            ButtonOnCard(
                text = btn3DText, // TODO: at gøre til parameter
                onClick = { },
                modifier = Modifier.align(Alignment.Center))

            InfoIcon(
                onClick = { showInfo = !showInfo }, // toggles info when clicked
                modifier = Modifier.align(Alignment.TopEnd)
            )
            if (showInfo) {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .matchParentSize(), // 👈 vigtigt: fylder hele kortet
                    contentAlignment = Alignment.Center
                ) {
                    InfoCard()
                }
            }

        }


    }
}

@Composable
fun ButtonOnCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {

    Button(
        modifier= modifier,
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF5F00)
        )
    ) {
        Text(text)
    }
}

@Composable
fun InfoIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            tint = Color.White
        )
    }
}

@Composable
fun InfoCard(modifier: Modifier = Modifier){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F6EE)
        )
    ) {

        Text(
            text = "Vores smarte 3D-scanner er en af de nemmeste måder at måle dig selv på med en 97% nøjagtighed.",
            modifier = Modifier.padding(20.dp)
        )
    }
}