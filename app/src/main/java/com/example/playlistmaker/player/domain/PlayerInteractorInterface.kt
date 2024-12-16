package com.example.playlistmaker.player.domain

import android.content.Context
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.search.domain.models.Track

interface PlayerInteractorInterface {
    fun setPlayer(
        previewUrl: String,
        context: Context,
        state: (PlaybackStatus) -> Unit,
        onCompletion: () -> Unit
    )
    fun pause()
    fun reset()
    fun playOrPauseAction(): PlaybackStatus
    fun playerGetCurrentTime(): Long
    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist)
}