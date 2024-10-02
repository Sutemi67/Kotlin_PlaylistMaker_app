package com.example.playlistmaker.player.domain

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlaybackStatus

interface PlayerRepositoryInterface {
    fun setPlayer(previewUrl: String, context: Context): PlaybackStatus
    fun stop(): PlaybackStatus.Ready
    fun pause(): PlaybackStatus.Paused
    fun play(): PlaybackStatus.Playing
    fun reset()
    fun playOrPauseAction(): PlaybackStatus
    fun player(): MediaPlayer
}