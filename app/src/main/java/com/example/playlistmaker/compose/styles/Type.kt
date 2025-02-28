package com.example.playlistmaker.compose.styles

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

val mediumTextStyle = TextStyle(
    textAlign = TextAlign.Start,
    fontSize = 24.sp,
    fontFamily = FontFamily(Font(R.font.ys_display_medium))
)
val regularTextStyle = TextStyle(
    textAlign = TextAlign.Start,
    fontSize = 16.sp,
    color = yp_black,
    fontWeight = FontWeight(400),
    fontFamily = FontFamily(Font(R.font.ys_display_regular))
)