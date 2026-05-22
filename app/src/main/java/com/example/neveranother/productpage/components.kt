package com.example.neveranother.productpage

import android.os.Build.VERSION.SDK_INT
import android.view.RoundedCorner
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.neveranother.R
import com.example.neveranother.components.NABodyText
import com.example.neveranother.components.NAHeader2
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.ui.theme.NeverAnotherTheme


@Composable
fun Dropdown(
    title: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // Smooth animation for height change when opening and closing dropdown
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded } // Executes the action
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NABodyText(title)

            if (isExpanded) {
                Icon(
                    painter = painterResource(R.drawable.expanded),
                    contentDescription = "Arrow that expands dropdown",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.collapsed),
                    contentDescription = "Arrow that expands dropdown",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
        }
        if (isExpanded) {
            content()
        }

    }
}

@Composable
fun Navigation() {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 140.dp)
    ) {
        NABodyText("NeverAnother")
    }
}


@Composable
fun Product() {

    var isColorSelected by remember { mutableStateOf(true) }

    var quantity by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth(0.90f)
    ) {
        val productImage =
            if (isColorSelected) R.drawable.na_prod_white else R.drawable.na_prod_black

        // Images updates based on state
        Image(
            painter = painterResource(id = productImage),
            contentDescription = "Product images",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Changes text depending on button press
            if (isColorSelected) NABodyText("Farve: " + " white") else NABodyText("Farve: " + " black")


            Row() {
                Icon(
                    painter = painterResource(R.drawable.white_circle),
                    contentDescription = "White color circle",
                    modifier = Modifier
                        .clickable { isColorSelected = true },
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.black_circle),
                    contentDescription = "Black color circle",
                    modifier = Modifier
                        .clickable { isColorSelected = false }
                )
            }

        }
        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NABodyText("799,99 kr")

            Row() {
                Icon(
                    painter = painterResource(R.drawable.minus_icon),
                    contentDescription = "minus icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .clickable {
                            if (quantity > 1) quantity--
                        }
                )

                Spacer(modifier = Modifier.width(5.dp))

                NABodyText(quantity.toString())

                Spacer(modifier = Modifier.width(5.dp))

                Icon(
                    painter = painterResource(R.drawable.plus_icon),
                    contentDescription = "plus icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .clickable {
                            quantity++
                        }
                )
            }

        }
        Column(

        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA3A3A3)
                )
            ) {
                NABodyText("Tilføj til kurv")
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 5.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.info_icon),
                contentDescription = "Info icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            NABodyText("Indtast mål før du kan ligge i kurv")
        }
    }
}

@Composable
fun BorderLine() {
    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 16.dp),
        thickness = 2.dp,
        color = Color.LightGray
    )
}


@Composable
fun Measurement() {
    Column(
        modifier = Modifier.fillMaxWidth(0.90f)
    ) {
        Button(
            onClick = {},
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF5F00)
            )
        ) {
            NABodyText("Tag mine mål", color = NAbackgroundColor)
        }
        Dropdown("Jeg har allerede mine mål", { dropdownContent1() })
    }
}

@Composable
fun dropdownContent1() {
    Column(
        modifier = Modifier.fillMaxWidth(0.90f)
    ) {
        Row() {
            GIFLoader(modifier = Modifier
                .padding(horizontal = 20.dp), R.drawable.bra_size)
        }
        Row() {
            Text("Hej Hej")
        }
        Row() {
            Text("Let it ride")
        }
    }
}

@Composable
fun Readmore(text: String) {
    var expanded by remember { mutableStateOf(false) }
    val preview = text.take(120) + if (text.length > 120) "…" else ""

    Column(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .padding(vertical = 8.dp)
    ) {
        NABodyText(if (expanded) text else preview)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (expanded) "Vis mindre" else "Læs mere",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.clickable { expanded = !expanded }
        )
    }
}

@Composable
fun ProductInfo(iconRes: Int, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            NAHeader2(title)
            Spacer(modifier = Modifier.height(4.dp))
            NABodyText(description)
        }
    }
}

@Composable
fun DropdownContent2(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun FAQ() {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .padding(vertical = 8.dp)
    ) {
        NAHeader2("Ofte stillede spørgsmål")
        Spacer(modifier = Modifier.height(8.dp))
        Dropdown("Hvad er størrelsesgarantien?") {
            DropdownContent2("Hvis pasformen ikke er perfekt, tilbyder vi en gratis ombytning til en ny størrelse. Kontakt os inden for 30 dage efter modtagelse.")
        }
        Dropdown("Hvornår modtager jeg min ordre?") {
            DropdownContent2("Vi bestræber os på at levere inden for 6 uger fra ordredato.")
        }
        Dropdown("Hvordan vasker jeg min bh?") {
            DropdownContent2("30 grader finvask. Brug vaskepose. Må ikke tørretumbles.")
        }
    }
}

@Composable
fun GIFLoader(
    modifier: Modifier = Modifier,
    gifResource: Int
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(gifResource)
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = "Animated GIF",
        modifier = modifier
    )
}