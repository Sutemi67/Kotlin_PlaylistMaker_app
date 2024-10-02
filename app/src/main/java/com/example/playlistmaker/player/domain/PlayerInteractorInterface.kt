package com.example.playlistmaker.player.domain

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlaybackStatus

interface PlayerInteractorInterface {
    fun setPlayer(previewUrl: String, context: Context, state: (PlaybackStatus) -> Unit)
    fun pause()
    fun reset()
    fun playOrPauseAction(): PlaybackStatus
    fun player(): MediaPlayer
    fun stopping(): PlaybackStatus.Ready
}