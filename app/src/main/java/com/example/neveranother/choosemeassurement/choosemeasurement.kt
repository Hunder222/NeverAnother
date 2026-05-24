package com.example.neveranother.choosemeassurement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.example.neveranother.viewmodels.NAViewModel

// code written by Rosalina

//these are the texts for the page in differnt languages
val btn3DText: String = "3D-scan"

val importantTextDanish: String = "Alt data bliver slettet efter brug af 3D-scanneren. Bruges med tætsiddende tøj."
val importantTextEnglish: String = "All data is deleted after using the 3D scanner. Use with tight-fitting clothing."

val btnManualDanish: String = "Manuel"
val btnManualEnglish: String = "Manual"

val infoText3DDanish: String = "Vores smarte 3D-scanner er en af de nemmeste måder at måle dig selv på med en 97% nøjagtighed."
val infoText3DEnglish: String ="Our smart 3D-scanner is the easiest way to meassure yourself. It has a 97% accuracy."

val infoTextManualDanish: String = "Du vil følge en step by step guide der både kan være en visuel eller video guide ud fra hvad du har lyst til. Du kommer igennem 5 steps. Der skal du blandt andet bruge et målebånd for at kunne måle øvre omkreds, nedre omkreds, brystbredde og brysthøjde. Til sidst ville du få en liste over de mål du har lavet."
val infoTextManualEnglish: String ="You will follow a step by step guide that can be either a visual or video guide based on what you want. You will go through 5 steps. Among other things, you will need a measuring tape to measure the upper circumference, lower circumference, chest width and chest height. At the end, you will get a list of the measurements you have made"


val firstTextReminderBtnDanish: String = "Har du ikke tid lige nu?"
val secondTextReminderBtnDanish: String ="Påmind mig senere"
val firstTextReminderBtnEnglish: String ="Don't have the time now?"
val secondTextReminderBtnEnglish: String ="Remind me later"

    @Composable
fun Choosemeasurement(
        naViewModel: NAViewModel, // references navigator so the buttons lead to the correct pages
        goToManual: () -> Unit,
        goToScanner: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Vælg Målemetode",
            fontFamily = NohemiFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        MeassurementCard( // 3D-card
            R.drawable.threed,
            "Image of woman scanning herself with her phone",
            btn3DText,
            goToScanner,
            infoText3DDanish
        )

        Row( // the icon and text needs to be next to each other so a row is used
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Infoicon",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 4.dp)
            )

            Text(
                text = importantTextDanish,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }

        MeassurementCard( // manual
            R.drawable.meassureself,
            "Image of woman meassuring herself with a soft tape meassure",
            btnManualDanish,
            goToManual,
            infoTextManualDanish
        )

        ReminderButton ({},firstTextReminderBtnDanish,secondTextReminderBtnDanish)
    }
}
@Composable
fun MeassurementCard( // use same for both so that changes can be made for both in one place
    image: Int,
    imageDescription: String,
    buttonText: String,
    onClick: () -> Unit,
    infoTextInLanguage: String) {

    var showInfo by remember { // remembers whether info should be shown
        mutableStateOf(false) // does so that info is automatically not shown
    }

    Card( // groups category and makes shape correct with rounded corners
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Box { // box used so things can be placed on top of each other
            Image(
                painter = painterResource(id = image),
                contentDescription = imageDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            ButtonOnCard(
                text = buttonText,
                onClick = onClick,
                modifier = Modifier.align(Alignment.Center))

            InfoIcon(
                onClick = { showInfo = !showInfo }, // toggles info when clicked
                modifier = Modifier.align(Alignment.TopEnd)
            )
            if (showInfo) {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .matchParentSize(), // won't exit parent card
                    contentAlignment = Alignment.Center
                ) {
                    InfoCard(infoText = infoTextInLanguage)
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
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
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
            contentDescription = "Infoicon",
            tint = Color.White
        )
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    infoText: String
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F6EE)
        )
    ) {

        Text(
            text = infoText,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun ReminderButton(
    onClick: () -> Unit,
    firstTextReminderBtn: String,
    secondTextReminderBtn: String
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB58A)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {

            Text(
                text = firstTextReminderBtn,
                fontSize = 12.sp,
                color = Color.White
            )

            Text(
                text = secondTextReminderBtn,
                fontSize = 22.sp,
                color = Color.White
            )

        }
    }
}