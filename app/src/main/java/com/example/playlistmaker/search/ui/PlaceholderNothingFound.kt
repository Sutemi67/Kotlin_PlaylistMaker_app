package com.example.playlistmaker.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.R

@Composable
fun PlaceholderNothingFound() {
    Column() {
        Image(
            painter = painterResource(R.drawable.img_nothing_found_light),
            contentDescription = "nothing found"
        )
        Text("Ничего не нашлось")
    }
}

@Preview(showBackground = true)
@Composable
fun Kkddd() {
    PlaceholderNothingFound()
}