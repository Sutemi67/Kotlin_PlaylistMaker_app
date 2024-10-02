package com.example.playlistmaker.player.ui

import android.content.Context
import android.media.MediaPlayer
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
        interactor.setPlayer(previewUrl, context, state = { state ->
            playbackStatus.postValue(state)
        })
    }

    fun player(): MediaPlayer = interactor.player()

    fun playOrPauseAction() {
        playbackStatus.postValue(interactor.playOrPauseAction())
    }

    fun stopping() {
        playbackStatus.postValue(interactor.stopping())
    }

    fun pausing() {
        interactor.pause()
    }

    fun reset() {
        interactor.reset()
    }
}