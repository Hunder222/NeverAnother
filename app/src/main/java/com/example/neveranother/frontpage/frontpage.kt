package com.example.neveranother.frontpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.neveranother.ui.theme.NAaccentColor
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import com.example.neveranother.productpage.ProductInfo

val nohemiBlack = FontFamily(
    Font(R.font.nohemi_black)
)
val warmGrey = Color(0xFFF9F6EE)

@Composable
fun Navbar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White.copy(alpha = 0.8f))
            .padding(horizontal = 16.dp)
    ) {

        // Left icon
        Icon(
            painter = painterResource(id = R.drawable.dehaze_24px),
            contentDescription = "Menu icon",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(28.dp)
        )

        // Center logo
        Icon(
            painter = painterResource(id = R.drawable.logo_black),
            contentDescription = "Nav bar logo",
            modifier = Modifier
                .align(Alignment.Center)
                .height(32.dp)
        )

        // Right icon
        Icon(
            painter = painterResource(id = R.drawable.shopping_bag_24px),
            contentDescription = "Shopping bag icon",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(28.dp)
        )
    }
}

@Composable
fun Frontpage() {

    // Scaffold to make sure the Navbar is on top of other content.
    Scaffold(
        topBar = {
            Navbar()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(top = 0.dp) // IMPORTANT: remove top padding
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.bh_forsidebillede1),
                    contentDescription = "Frontpage image 1"
                )

                Text(
                    text = "En BH tilpasset dine mål",
                    fontFamily = nohemiBlack,
                    fontSize = 26.sp,
                    modifier =
                        Modifier.padding(top = 245.dp, start = 15.dp),


                    )
            }
            Box {
                Image(
                    painter = painterResource(id = R.drawable.bh_forsidebillede2),
                    contentDescription = "Frontpage image 2"
                )

                Button(
                    onClick = {
                        // Do something
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NAaccentColor
                    ),
                    modifier = Modifier
                        .padding(top = 270.dp, start = 15.dp)
                        .width(150.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.3f),
                            spotColor = Color.Black
                        )
                ) {
                    Text(
                        text = "Se produkt",
                        fontSize = 20.sp
                    )
                }

                Text(
                    text = "Dansk design \nskræddersyet i Holland",
                    fontFamily = nohemiBlack,
                    fontSize = 26.sp,
                    modifier =
                        Modifier.padding(top = 330.dp, start = 15.dp)
                )

            }

            Box {
                Image(
                    painter = painterResource(id = R.drawable.bh_forsidebillede3),
                    contentDescription = "Frontpage image 3",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )


                Text(
                    text = "Sig farvel til standardstørrelser",
                    fontFamily = nohemiBlack,
                    fontSize = 26.sp,
                    modifier =
                        Modifier.padding(top = 310.dp, start = 15.dp)
                )
            }

            /*
        Text(
            text = "Sig farvel til standardstørrelser",
            fontFamily = nohemiBlack,
            fontSize = 26.sp,
            modifier = Modifier.padding(top = 40.dp, start = 15.dp)
        )
         */

            Text(
                text = "Traditionelle BH-størrelser har aldrig passet til alle kroppe.\n" +
                        "\n" +
                        "NEVER ANOTHER ændrer det med en digitalt skræddersyet, bøjlefri BH tilpasset ud fra dine egne mål. Den sømløse 3D-knit teknologi skaber en blødere og mere behagelig pasform, der føles helt som din egen.\n" +
                        "\n" +
                        "Tag dine mål derhjemme.",
                //fontFamily = nohemiBlack,
                fontSize = 16.sp,
                modifier =
                    Modifier.padding(top = 70.dp, start = 15.dp)
            )
            Text(
                text = "Vi guider dig hele vejen.",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier =
                    Modifier.padding(top = 0.dp, start = 15.dp)
            )

            Text(
                text = "Når pasform føles \npersonlig",
                fontFamily = nohemiBlack,
                fontSize = 26.sp,
                modifier = Modifier.padding(top = 70.dp, start = 15.dp)
            )
            Text(
                text = "Hvad vores kunder siger",
                //fontFamily = nohemiBlack,
                fontSize = 26.sp,
                modifier = Modifier.padding(top = 0.dp, start = 15.dp)
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier
                        .padding(15.dp, top = 40.dp)
                        .background(
                            color = warmGrey,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                        .width(300.dp)
                ) {

                    Text(
                        text = "Gitte",
                        fontFamily = nohemiBlack,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Bh'en sidder helt perfekt på min krop. " +
                                "Jeg er virkelig glad for den, og jeg føler mig meget veltilpas i den. " +
                                "Stoffet og pasformen passer mig rigtig godt. " +
                                "Algoritmen virker!",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(15.dp, top = 40.dp)
                        .background(
                            color = warmGrey,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                        .width(300.dp)
                ) {

                    Text(
                        text = "Teresa",
                        fontFamily = nohemiBlack,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Denne bh er præcis som lovet. Den er blød, strammer ikke og føles ikke ubehagelig nogen steder, og jeg bemærker slet ikke, at jeg har en bh på, selvom jeg bruger en J-skål.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(15.dp, top = 40.dp)
                        .background(
                            color = warmGrey,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp, end = 60.dp)
                        .width(300.dp)
                ) {

                    Text(
                        text = "Birthe",
                        fontFamily = nohemiBlack,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "“This bra was made for you” stod der på pakken, da jeg modtog min bh fra Never Another. De har ret! Den er perfekt skræddersyet til mig, den løfter og samler som en bøjle-bh, og ’stof-bøjlen’ irriterer overhovedet ikke. Kan varmt anbefales.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                //Horisontal Row ending here
            }


            Box (modifier = Modifier.padding(top = 40.dp)){
                Image(
                    painter = painterResource(id = R.drawable.bh_forsidebillede4),
                    contentDescription = "Frontpage image 4",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Text(
                    text = "Pasformsgaranti",
                    fontFamily = nohemiBlack,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(top = 345.dp, start = 15.dp),
                    color = Color.White
                )

            }




            Column(
                modifier = Modifier
                    .padding(start = 0.dp, top = 30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
            ProductInfo(
                R.drawable.check_mark,
                "Størrelsesgaranti",
                "Vi ønsker, at du skal elske din bh. Pasformen er ikke perfekt? Vi tilbyder en gratis størrelsesgaranti! Se venligst ofte stillede spørgsmål nedenfor for at få mere at vide."
            )
            }
/*
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .background(
                        color = warmGrey,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp, end = 60.dp)
                    .width(300.dp)
                    .height(300.dp)
            ) {}


 */
            Button(
                onClick = {
                    // Do something
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NAaccentColor
                ),
                modifier = Modifier
                    .padding(top = 10.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 3.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.3f),
                        spotColor = Color.Black
                    )
            ) {
                Text(
                    text = "Se produkt",
                    fontSize = 20.sp
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .background(color = Color.Red.copy(alpha = 0.5f))
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.logo_black),
                    contentDescription = "Nav bar logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 40.dp))

                Text (
                    text = "Kontakt",
                    fontSize = 16.sp
                )

                Text(
                    text = "E-mail: Customercare@neveranother.dk",
                    fontSize = 16.sp
                )

                Text(
                    text = "Never Another ApS, CVR: 43577662",
                    fontSize = 16.sp
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrontpagePreview() {
    Frontpage()
}