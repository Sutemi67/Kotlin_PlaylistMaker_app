package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.data.PlaybackStatus

interface PlayerRepositoryInterface {
    fun setPlayer(previewUrl: String, onCompletion: () -> Unit): PlaybackStatus
    fun stop(): PlaybackStatus.Ready
    fun pause(): PlaybackStatus.Paused
    fun play(): PlaybackStatus.Playing
    fun reset()
    fun playOrPauseAction(): PlaybackStatus
    fun playerGetCurrentTime(): Long
}