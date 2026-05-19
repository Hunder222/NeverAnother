package com.example.neveranother.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.neveranother.R

// Never Another fonts
val NohemiFontFamily = FontFamily(
    Font(R.font.nohemi_thin, FontWeight.Thin),
    Font(R.font.nohemi_extralight, FontWeight.ExtraLight),
    Font(R.font.nohemi_light, FontWeight.Light),
    Font(R.font.nohemi_regular, FontWeight.Normal),
    Font(R.font.nohemi_medium, FontWeight.Medium),
    Font(R.font.nohemi_semibold, FontWeight.SemiBold),
    Font(R.font.nohemi_bold, FontWeight.Bold),
    Font(R.font.nohemi_extrabold, FontWeight.ExtraBold),
    Font(R.font.nohemi_black, FontWeight.Black)
)



// Google fonts setup
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// "Inter" font from Google fonts
val interFontName = GoogleFont("Inter")

// FontFamily for "Inter"
val Inter = FontFamily(
    Font(googleFont = interFontName, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = interFontName, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = interFontName, fontProvider = provider, weight = FontWeight.Bold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)