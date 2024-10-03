package com.example.playlistmaker.player.domain

import android.content.Context
import com.example.playlistmaker.player.data.PlaybackStatus

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
}