package com.febin.theevangelist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.febin.theevangelist.R  // Import R for font resources

// Custom FontFamily for Outfit (assuming fonts in res/font: outfit_regular.ttf, outfit_medium.ttf, outfit_bold.ttf, etc.)
val Outfit = FontFamily(
    Font(R.font.outfit_thin, FontWeight.Thin),      // 100
    Font(R.font.outfit_extralight, FontWeight.ExtraLight),  // 200
    Font(R.font.outfit_light, FontWeight.Light),    // 300
    Font(R.font.outfit_regular, FontWeight.Normal), // 400
    Font(R.font.outfit_medium, FontWeight.Medium),  // 500
    Font(R.font.outfit_semibold, FontWeight.SemiBold), // 600
    Font(R.font.outfit_bold, FontWeight.Bold),      // 700
    Font(R.font.outfit_extrabold, FontWeight.ExtraBold), // 800
    Font(R.font.outfit_black, FontWeight.Black)     // 900
)

// Custom Typography using Outfit font family
val Typography = Typography(
    // Body styles (e.g., for regular text)
    bodySmall = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Title styles (e.g., for subtitles or labels)
    titleSmall = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Headline styles (e.g., for main headings)
    headlineSmall = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    )
    // Add more styles if needed, e.g., labelSmall, displayLarge, etc.
)