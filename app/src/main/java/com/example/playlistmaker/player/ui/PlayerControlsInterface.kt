package com.example.playlistmaker.player.ui

import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerControlsInterface {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun startForeground()
    fun stopForeground()
}