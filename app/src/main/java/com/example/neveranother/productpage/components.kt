package com.example.neveranother.productpage

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
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
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.ui.theme.NAtextBlack

// Reusable dropdown function
@Composable
fun Dropdown(
    title: String,
    fontSize: TextUnit = 20.sp,
    content: @Composable () -> Unit // Can pass another function inside composable
) {
    var isExpanded by remember { mutableStateOf(false) } // Remembers if the dropdown is open or closed

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // Smooth animation for height change when opening and closing dropdown
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded } // Flips the value from true to false
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NABodyText(title, fontSize = fontSize, modifier = Modifier.weight(1f))

            // Checks the isExpanded state to decide which arrow to show
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
        // Shows content if isExpanded is true
        if (isExpanded) {
            content()
        }
    }
}

// Top navigation (Mathias have hopefully made it)
@Composable
fun Navigation() {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 100.dp)
    ) {
        NABodyText("NeverAnother")
    }
}

// Product part
@Composable
fun Product() {
// Tracks if the white or black icon is active
    var isColorSelected by remember { mutableStateOf(true) }

// Tracks how many items the user wants to buy
    var quantity by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth(0.90f)
    ) {
        // Checks the state if true it picks white image, otherwise it picks black image
        val productImage =
            if (isColorSelected) R.drawable.na_prod_white else R.drawable.na_prod_black

        // Images updates based on state
        Image(
            painter = painterResource(id = productImage),
            contentDescription = "Product images",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
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
                        // When clicked it shows white bra
                        .clickable { isColorSelected = true },
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.black_circle),
                    contentDescription = "Black color circle",
                    modifier = Modifier
                        // When clicked it shows black bra
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
                        // When clicked it makes sure quantity can't go below 1
                        .clickable {
                            if (quantity > 1) quantity--
                        }
                )

                Spacer(modifier = Modifier.width(5.dp))
                // Updates the counter each time you click
                NABodyText(quantity.toString())

                Spacer(modifier = Modifier.width(5.dp))

                Icon(
                    painter = painterResource(R.drawable.plus_icon),
                    contentDescription = "plus icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        // When clicked it adds one to the counter
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
    BorderLine()
}

// Reusable borderline.
@Composable
fun BorderLine() {
    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 10.dp),
        thickness = 2.dp,
        color = Color.LightGray
    )
}

// Measurement part
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
        Dropdown("Jeg har allerede mine mål") { dropdownContent1() }
    }
    BorderLine()
}

// DropwdownContent 1 for measurement dropdown. Handles measurement data from user
@Composable
fun dropdownContent1() {
    // Keeps the data even if user rotates phone
    var upperCircum by rememberSaveable { mutableStateOf("") }
    var lowerCircum by rememberSaveable { mutableStateOf("") }
    var chestWidth by rememberSaveable { mutableStateOf("") }
    var chestHeight by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            // Calls the GIFLoader composable
            GIFLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                R.drawable.bra_size
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                upperCircum,
                onValueChange = { upperCircum = it },
                label = { Text("Øvre omkreds", fontSize = 15.sp) },
                modifier = Modifier.weight(1f),
                // Makes sure user can only type numbers
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )

            OutlinedTextField(
                lowerCircum,
                onValueChange = { lowerCircum = it },
                label = { Text("Nedre omkreds", fontSize = 15.sp) },
                modifier = Modifier.weight(1f),
                // Makes sure user can only type numbers
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            OutlinedTextField(
                chestWidth,
                onValueChange = { chestWidth = it },
                label = { Text("Brystbredde", fontSize = 15.sp) },
                modifier = Modifier.weight(1f),
                // Makes sure user can only type numbers
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )

            OutlinedTextField(
                chestHeight,
                onValueChange = { chestHeight = it },
                label = { Text("Brysthøjde", fontSize = 15.sp) },
                modifier = Modifier.weight(1f),
                // Makes sure user can only type numbers
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
        }
        // Calls the DropdownSelector to show breast volume
        DropdownSelector()
    }
}

// Data class for DropdownSelector
// Used to pair text and images together
data class MenuOptions(
    val text: String,
    val image: Int
)

// DropdownSelector for breast volume in measurement
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector() {
    // A list of all four options using the data class
    val options = listOf(
        MenuOptions("Fastere top", R.drawable.firmer_top_volumen),
        MenuOptions("Blødere top", R.drawable.softer_top_volumen),
        MenuOptions("Fastere bund", R.drawable.firmer_bottom_volumen),
        MenuOptions("Blødere bund", R.drawable.softer_bottom_volumen),
    )

    // Keeps the dropdown closed
    var expanded by remember { mutableStateOf(false) }
    // Starts off with Volume and changes when you press on an option.
    // Resets if you close the Dropdown composable
    var selectedOption by remember { mutableStateOf("Volume") }

    Column() {
        // Handles the logic of a dropdown field
        ExposedDropdownMenuBox(
            expanded = expanded,
            // Toggles the menu open or closed when user taps the field
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    // Makes sure the popup menu appears relative to the text field
                    .menuAnchor(),
                // Makes sure the user can't type in the field
                readOnly = true,
                // Shows the selected volume
                value = selectedOption,
                onValueChange = {},
                // Arrow that rotates depending on state
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // loops through the list and creates dropDownMenuItem for each one
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
                            // Updates selectedOption with selected text when clicked
                            selectedOption = option.text
                            // Closes the menu when clicked
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Reusable GIFLoader used with the Coil library
@Composable
fun GIFLoader(
    modifier: Modifier = Modifier,
    gifResource: Int
) {
    val context = LocalContext.current
    // Builds a custom ImageLoader that knows how to read GIF files
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
        // Draws image. Loads GIF in background so app doesn't freeze
        painter = rememberAsyncImagePainter(
            // Tells Coil which GIF to load
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

// Readmore part
@Composable
fun Readmore(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3
) {
    // Tracks whether the user sees short text or full text
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth(0.90f)
    ) {
        Text(
            text = text,
            // If readmore is closed it limits text to three lines
            // If readmore is open, it shows full text
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            // When text is collapsed it shows ... to tell user there is more to read
            overflow = TextOverflow.Ellipsis
        )

        Text(
            // Shows "læs mindre" if text is open otherwise shows "læs mere" when collapsed
            text = if (isExpanded) "Læs mindre" else "Læs mere",
            color = NAtextBlack,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                // Flips state when clicking text
                .clickable { isExpanded = !isExpanded }
                .padding(top = 4.dp)
        )
    }
}

// Reusable ProductInfo
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

// DropdownContent2 for all other dropdowns used
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

// FAQ part
@Composable
fun FAQ() {
    Column(
        modifier = Modifier.fillMaxWidth(0.90f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NAHeader1("FAQ", fontSize = 50.sp)

        BorderLine()

        Dropdown("Hvornår vil jeg modtage min ordre?", fontSize = 16.sp) {
            DropdownContent2("Vi bestræber os på at levere din bh inden for 6 uger. NEVER ANOTHER er stadig en startup, og da vi arbejder med produktionsvinduer og målsyede produkter, er leveringstiderne længere, end hvad fast fashion-branchen normalt kan tilbyde. Vi gør vores bedste for at minimere ventetiden, og vi takker for din tålmodighed.")
        }

        BorderLine()

        Dropdown("Hvilke lande fragter i til?", fontSize = 16.sp) {
            DropdownContent2(
                "Vi sender i øjeblikket til følgende lande:\n" +
                        "Danmark, Østrig, Belgien, Frankrig, Tyskland, Italien, Luxembourg, Holland og Sverige.\n" +
                        "\n" +
                        "Pakkerne sendes fra Danmark.\n" +
                        "Hvis du ønsker, at vi skal sende til et land, der ikke er på listen, bedes du sende os en besked."
            )
        }

        BorderLine()

        Dropdown("Kan jeg få hjælp til at måle mig selv?", fontSize = 16.sp) {
            DropdownContent2(
                "Ja! Hvis du synes, det er svært at gennemgå guidevideoerne, kan du arrangere en online prøvning ved at booke en tid her.\n" +
                        "\n" +
                        "Hvis der ikke er flere ledige tider, eller de tilgængelige tider ikke passer dig, bedes du tjekke igen snart eller sende os en besked."
            )
        }

        BorderLine()

        Dropdown("Kan jeg returnere min ordre?", fontSize = 16.sp) {
            DropdownContent2(
                "Når du afgiver en ordre, fremstiller vi et skræddersyet produkt, der ikke er en standardstørrelse, og som ikke kan videresælges. På grund af karakteren af vores specialfremstillede produkter tilbyder vi derfor ikke fuld returret.\n" +
                        "\n" +
                        "Vi tilbyder dog en fuld bytteservice, hvis det leverede produkt ikke stemmer overens med ordren, for eksempel hvis du har modtaget en forkert farve, eller hvis produktet har fabrikationsfejl.\n" +
                        "Hvis din bh imidlertid ikke passer dig, tilbyder vi en gratis størrelsesgaranti. Se venligst nedenfor."
            )
        }

        BorderLine()

        Dropdown("Hvad hvis min BH ikke passer mig?", fontSize = 16.sp) {
            DropdownContent2(
                "Hvis din bh ikke passer dig, tilbyder vi en gratis størrelsesgaranti. I det tilfælde vil vi justere målene og sende en ny bh uden beregning.\n" +
                        "\n" +
                        "For at være berettiget til en ombytning skal du deltage i et videoopkald sammen med os, så vi kan identificere problemet og finde en løsning.\n" +
                        "\n" +
                        "Bemærk venligst, at mange kroppe ændrer sig i størrelse i løbet af menstruationscyklussen. Hvis din bh ikke passer ved levering, bør du overveje, om det kan skyldes timingen i din cyklus.\n" +
                        "\n" +
                        "Vores mål er at tilbyde dig den bedst mulige pasform, og vi hører altid gerne din feedback.\n" +
                        "\n" +
                        "Hvis du har brug for at benytte vores størrelsesgaranti, bedes du kontakte os her.\n" +
                        "\n" +
                        "Du kan læse mere om vores størrelsesgaranti i vores handelsbetingelser."
            )
        }

        BorderLine()

        Dropdown("Er alle størrelser tilgængelige?", fontSize = 16.sp) {
            DropdownContent2(
                "NEVER ANOTHER er en startup-virksomhed – vi har en mission om at tilbyde den bedst mulige bh til alle kropstyper og størrelser. Det er en lang rejse, og vi har været nødt til at sikre os, at vores design fungerer perfekt til et bestemt størrelsesinterval i første omgang.\n" +
                        "\n" +
                        "Det betyder, at we endnu ikke har et produkt til alle, men vi når dertil, og vi arbejder hårdt på at sikre, at du også kan få din personlige NEVER ANOTHER-bh.\n" +
                        "\n" +
                        "Start med at designe din bh og gennemfør de første 2 måletrin for at se, om vi kan lave en til dig. Hvis det viser sig, at vi endnu ikke har en bh til dig, så tilmeld dig vores nyhedsbrev for at være den første, der får besked, når vi har."
            )
        }

        BorderLine()

        Dropdown("Hvor er NEVER ANOTHER fra?", fontSize = 16.sp) {
            DropdownContent2("Vi er danske med kontorer i København og Aarhus. Vi producerer i Holland hos vores betroede produktionspartner.")
        }

        BorderLine()
    }
}
