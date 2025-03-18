package com.example.playlistmaker.main.ui.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

val Typography = Typography(

    bodySmall = TextStyle(
        textAlign = TextAlign.Start,
        fontSize = 16.sp,
        fontWeight = FontWeight(400),
        fontFamily = FontFamily(Font(R.font.ys_display_regular))
    ),
    bodyMedium = TextStyle(
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.ys_display_medium))
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
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
)

val playlistInfo = TextStyle(
    textAlign = TextAlign.Start,
    fontSize = 12.sp,
    fontWeight = FontWeight(400),
    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
    letterSpacing = 0.4.sp
)