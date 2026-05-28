package com.example.neveranother.scanner.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.R
import com.example.neveranother.components.NABodyText
import com.example.neveranother.components.NAHeader1
import com.example.neveranother.components.NAHeader2
import com.example.neveranother.components.NAMicroCopy
import com.example.neveranother.ui.theme.NAaccentColor
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.ui.theme.NAtextBlack
import com.example.neveranother.ui.theme.NAwarmGrey


var header1Text = "3D scanner"
var header2Text = "Sådan bruger du scanneren"
var bodyText1 = "Brug bagkameraet på din telefon til at scanne øvre torso. \n" +
        "- Klik start scan og stå helt stille, panorér telefonen fra den ene til den anden side.\n" +
        "- Sørg for også at scanne siderne af kroppen. \n" +
        "- Kan scanne på Tætsidende tøj eller bare BH"
var microCopy1 = "Alt data om kropscanning slettes efterfølgende, og bliver behandlet på telefonen."
var buttonText1 = "Næste"



@Composable
fun HowToScan(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NAbackgroundColor)
            .padding(vertical = 20.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NAHeader1(
            text = header1Text
        )

        NAHeader2(
            text = header2Text,
            modifier = Modifier
                .padding(vertical = 30.dp),
        )

        Image(
            painter = painterResource(R.drawable.howtoscan2),
            contentDescription = "How to scan",
        )

        NABodyText(
            text = bodyText1,
            modifier = Modifier
                .padding(vertical = 20.dp),
        )


        // Laver plads der fylder resterene whitespace, så button skubbes ned i bunden af skærmen
        Spacer(modifier = Modifier.weight(1f))

        NAMicroCopy(microCopy1)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding()
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.33f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Tilbage", color = NAtextBlack, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onNextClick,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NAaccentColor),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 0.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = buttonText1,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}