package com.example.playlistmaker.player.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.player.domain.PlayerInteractorInterface

class PlayerViewModel(
    private val interactor: PlayerInteractorInterface
) : ViewModel() {

    private var playbackStatus = MutableLiveData<PlaybackStatus>(PlaybackStatus.Ready)
    fun getPlaybackLiveData(): LiveData<PlaybackStatus> = playbackStatus

    fun setPlayer(
        previewUrl: String,
        context: Context,
    ) {
        interactor.setPlayer(
            previewUrl = previewUrl,
            context = context,
            state = { state -> playbackStatus.postValue(state) },
            onCompletion = { playbackStatus.postValue(PlaybackStatus.Ready) }
        )
    }

    fun playOrPauseAction() {
        playbackStatus.postValue(interactor.playOrPauseAction())
    }

    fun pausing() {
        interactor.pause()
    }

    fun reset() {
        interactor.reset()
    }

    fun playerGetCurrentTime(): Long = interactor.playerGetCurrentTime()
}