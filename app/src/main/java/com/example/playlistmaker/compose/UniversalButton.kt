package com.example.playlistmaker.compose

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.main.ui.ui.theme.yp_black

@Composable
fun UniversalButton(text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = { onClick },
        colors = ButtonDefaults.buttonColors(
            containerColor = yp_black
        )
    ) {
        Text(text)
    }
}

@Preview
@Composable
fun Ldd() {
    UniversalButton("Обновить") { }
}