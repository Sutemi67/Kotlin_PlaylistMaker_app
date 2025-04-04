package com.example.playlistmaker.compose

import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme", showBackground = true)
annotation class ThemePreviews

enum class Errors {
    NoFavourites,
    NoPlaylists,
    SearchNoConnection,
    SearchNothingFound,
    NoTracksInPlaylist
}

fun formatTrackTime(millis: Int): String {
    val minutes = millis / 1000 / 60
    val seconds = millis / 1000 % 60
    return String.format("%02d:%02d", minutes, seconds)
}


//fun text() = when (model.count % 10) {
//    0 -> "Нет треков"
//    1 -> "${model.count} трек"
//    in 2..4 -> "${model.count} трека"
//    else -> "${model.count} треков"
//}

object JsonConverter {
    private val gson = Gson()
    fun trackToJson(track: Track): String {
        return gson.toJson(track)
    }

    fun jsonToTrack(json: String): Track {
        return gson.fromJson(json, Track::class.java)
    }

    fun playlistToJson(playlist: Playlist): String {
        return gson.toJson(playlist)
    }

    fun jsonToPlaylist(json: String): Playlist {
        return gson.fromJson(json, Playlist::class.java)
    }
}

val Int.iPx2Dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.iDp2Px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.fPx2Dp: Float get() = this / Resources.getSystem().displayMetrics.density
val Int.fDp2Px: Float get() = this * Resources.getSystem().displayMetrics.density
val Float.iPx2Dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Float.iDp2Px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.fPx2Dp: Float get() = this / Resources.getSystem().displayMetrics.density
val Float.fDp2Px: Float get() = this * Resources.getSystem().displayMetrics.density