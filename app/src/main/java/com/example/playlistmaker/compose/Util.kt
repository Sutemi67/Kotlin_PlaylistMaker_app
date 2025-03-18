package com.example.playlistmaker.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme",showBackground = true)
annotation class ThemePreviews

enum class Errors {
    NoFavourites,
    NoPlaylists,
    SearchNoConnection,
    SearchNothingFound
}


fun formatTime(millis: Int): String {
    val minutes = millis / 1000 / 60
    val seconds = millis / 1000 % 60
    return String.format("%02d:%02d", minutes, seconds)
}

object JsonConverter {
    private val gson = Gson()
    fun trackToJson(track: Track): String {
        return gson.toJson(track)
    }

    fun jsonToTrack(json: String): Track {
        return gson.fromJson(json, Track::class.java)
    }
}