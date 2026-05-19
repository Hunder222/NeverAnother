package com.example.neveranother.choosemeassurement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
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


val btn3DText: String = "3D-scan"

@Composable
fun Choosemeasurement(){
Scan3D()
}
@Composable
fun Scan3D(){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Box{
            Image(
                painter = painterResource(id = R.drawable.threed),
                contentDescription = "",
                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
            )
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FilledButtonExample {  }
            }
        }
    }
}


@Composable
fun FilledButtonExample(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF5F00)
        )
    ) {
        Text(btn3DText)
    }
}

