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
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
    ),
    bodyMedium = TextStyle(
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.ys_display_medium))
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontWeight = FontWeight(700),
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontWeight = FontWeight(500),
        fontSize = 19.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
        fontWeight = FontWeight(400),
        fontSize = 13.sp
    )
)

val playlistInfo = TextStyle(
    textAlign = TextAlign.Start,
    fontSize = 12.sp,
    fontWeight = FontWeight(400),
    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
    letterSpacing = 0.4.sp
)