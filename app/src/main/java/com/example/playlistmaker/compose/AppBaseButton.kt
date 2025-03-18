package com.example.playlistmaker.compose

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.main.ui.ui.theme.customButtonColors

@Composable
fun AppBaseButton(text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        colors = customButtonColors()
    ) {
        Text(text)
    }
}

@ThemePreviews
@Composable
fun Ldd() {
    PlaylistMakerTheme {
        Surface {
            AppBaseButton("Обновить") { }
        }
    }
}