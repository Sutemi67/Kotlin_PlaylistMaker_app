package com.example.playlistmaker.compose

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.main.ui.ui.theme.customButtonColors

@Composable
fun AppBaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick,
        colors = customButtonColors(),
        enabled = isEnabled
    ) {
        Text(text)
    }
}

@ThemePreviews
@Composable
fun Ldd() {
    PlaylistMakerTheme {
        Surface {
            AppBaseButton(
                text = "Обновить",
                onClick = {},
                modifier = Modifier,
                isEnabled = true
            )
        }
    }
}