package com.example.playlistmaker.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme",showBackground = true)
annotation class ThemePreviews

enum class Errors {
    NoFavourites,
    NoPlaylists,
    SearchNoConnection,
    SearchNothingFound
}