package com.example.playlistmaker.player.domain

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.search.domain.models.Track

interface PlayerRepositoryInterface {
    fun setPlayer(previewUrl: String, onCompletion: () -> Unit): PlaybackStatus
    fun stop(): PlaybackStatus.Ready
    fun pause(): PlaybackStatus.Paused
    fun play(): PlaybackStatus.Playing
    fun reset()
    fun playOrPauseAction(): PlaybackStatus
    fun playerGetCurrentTime(): Long
    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist)

}