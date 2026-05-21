package com.example.neveranother.productpage

import android.os.Build.VERSION.SDK_INT
import android.view.Menu
import android.view.RoundedCorner
import androidx.annotation.experimental.Experimental
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.neveranother.components.NAHeader1
import com.example.neveranother.components.NAHeader2
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.ui.theme.NAtextBlack
import com.example.neveranother.ui.theme.NeverAnotherTheme
import kotlin.math.round


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
            .padding(vertical = 10.dp, horizontal = 100.dp)
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
            if (isColorSelected) NABodyText("Farve: " + "white", fontWeight = FontWeight.SemiBold)
            else NABodyText("Farve: " + "black", fontWeight = FontWeight.SemiBold)


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
            NABodyText("799,99 kr", fontWeight = FontWeight.SemiBold)

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
        Column() {
            Button(
                onClick = {},
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {
                NABodyText("Tilføj til kurv", color = Color(0xFFFFFFFF))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.info_icon),
                contentDescription = "Info icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            NABodyText("Indtast mål før du kan ligge i kurv", fontSize = 15.sp)
        }
    }
}

@Composable
fun BorderLine() {
    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 10.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            GIFLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                R.drawable.bra_size
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var upperCircum by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                upperCircum,
                onValueChange = { upperCircum = it },
                label = { Text("Øvre omkreds") },
                modifier = Modifier.weight(1f)

            )

            var lowerCircum by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                lowerCircum,
                onValueChange = { lowerCircum = it },
                label = { Text("Nedre omkreds") },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            var chestWidth by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                chestWidth,
                onValueChange = { chestWidth = it },
                label = { Text("Brystbredde") },
                modifier = Modifier.weight(1f)
            )

            var chestHeight by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                chestHeight,
                onValueChange = { chestHeight = it },
                label = { Text("Brysthøjde") },
                modifier = Modifier.weight(1f)
            )
        }
        DropdownSelector()
    }
}


data class MenuOptions(
    val text: String,
    val image: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector() {

    val options = listOf(
        MenuOptions("Fastere top", R.drawable.firmer_top_volumen),
        MenuOptions("Blødere top", R.drawable.softer_top_volumen),
        MenuOptions("Fastere bund", R.drawable.firmer_bottom_volumen),
        MenuOptions("Blødere bund", R.drawable.softer_bottom_volumen),
    )

    var expanded by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("Volume") }

    Column() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = selectedOption,
                onValueChange = {},

                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = option.image),
                                    contentDescription = "${option.text} image",
                                    modifier = Modifier
                                        .size(90.dp)
                                        .background(Color.LightGray)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = option.text,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        },
                        onClick = {
                            selectedOption = option.text
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun GIFLoader(
    modifier: Modifier = Modifier,
    gifResource: Int
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    Image(
        painter = rememberAsyncImagePainter(
            model = remember(gifResource) {
                ImageRequest.Builder(context)
                    .data(gifResource)
                    .build()
            },
            imageLoader = imageLoader
        ),
        contentDescription = "Animated GIF",
        modifier = modifier
    )
}

@Composable
fun Readmore(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth(0.90f)
    ) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = if (isExpanded) "Læs mindre" else "Læs mere",
            color = NAtextBlack,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun ProductInfo(
    logo: Int,
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .border(width = 2.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(logo),
                contentDescription = "logo",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title)
        }
        Text(content)
    }
}

@Composable
fun DropdownContent2(
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .padding(16.dp)
    ) {
        Text(content)
    }
}

@Composable
fun FAQ() {
    Column(
        modifier = Modifier.fillMaxWidth(0.90f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NAHeader1("FAQ", fontSize = 50.sp)

    }
}
