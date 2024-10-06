package com.example.playlistmaker.player.data

sealed interface PlaybackStatus {
    data object Playing : PlaybackStatus
    data object Paused : PlaybackStatus
    data object Ready : PlaybackStatus
    data object Error : PlaybackStatus
}