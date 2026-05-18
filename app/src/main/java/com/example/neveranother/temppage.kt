package com.example.neveranother

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.neveranother.components.NABodyText
import com.example.neveranother.components.NAHeader1
import com.example.neveranother.components.NAHeader2
import com.example.neveranother.components.NAMicroCopy
import com.example.neveranother.ui.theme.Inter
import com.example.neveranother.ui.theme.NAaccentColor
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.example.neveranother.viewmodels.NAviewmodel

// This is a temporary view, used for development navigation, as proper navigation is not implemented yet
@Composable
fun Temppage(
    NAviewmodel: NAviewmodel,
    goToFrontpage: () -> Unit,
    goToProductpage: () -> Unit,
    goToChoosemeasurement: () -> Unit,
    goToManual: () -> Unit,
    goToScanner: () -> Unit,
    goToCart: () -> Unit,
){
    // container for buttons, centered on the screen
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // examples of text components for devs
        NAHeader1("Nohemi Header 1")
        NAHeader2("Nohemi Header 2")
        NABodyText("Inter Body Text")
        NAMicroCopy("Inter Micro copy text")

        //Buttons that uses callback functions to navigate to views
            // example on how to use never another colors in button
        Button(
            onClick = goToFrontpage,
            colors = ButtonDefaults.buttonColors(
                containerColor = NAaccentColor,
                contentColor = NAbackgroundColor
            )
        ){
            // example on how Inter font can be used
            Text(
                text = "Frontpage",
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 25.sp
            )
        }

        Button(
            onClick = goToProductpage
        ){
            Text("Productpage")
        }

        Button(
            onClick = goToChoosemeasurement
        ){
            Text("Choosemeasurement")
        }

        Button(
            onClick = goToManual
        ){
            Text("Manual")
        }

        Button(
            onClick = goToScanner
        ){
            Text("3D Scanner")
        }

        Button(
            onClick = goToCart
        ){
            Text("Cart")
        }
    }
}