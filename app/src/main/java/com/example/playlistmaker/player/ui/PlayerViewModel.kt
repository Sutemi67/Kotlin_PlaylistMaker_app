package com.example.playlistmaker.player.ui

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.player.domain.PlayerInteractorInterface
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val interactor: PlayerInteractorInterface,
    private val mainThreadHandler: Handler
) : ViewModel() {

    private var playbackStatus = MutableLiveData<PlaybackStatus>(PlaybackStatus.Ready)
    fun getPlaybackLiveData(): LiveData<PlaybackStatus> = playbackStatus
    private var counterData = MutableLiveData("")
    fun getCounterText(): LiveData<String> = counterData

    private val timeCounterRunnable: Runnable by lazy {
        Runnable {
            Log.e("timeCounterStart", "time counter started")
            counterData.value =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerGetCurrentTime())
            mainThreadHandler.postDelayed(timeCounterRunnable, 1000L)
        }
    }

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

    fun postOrRemoveCounter(value: Boolean) {
        if (value) mainThreadHandler.post(timeCounterRunnable)
        else mainThreadHandler.removeCallbacks(timeCounterRunnable)
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

    private fun playerGetCurrentTime(): Long = interactor.playerGetCurrentTime()
}