package com.example.playlistmaker.app

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import com.example.playlistmaker.player.ui.PlayerControlsInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerService() : Service(), PlayerControlsInterface {

    companion object {
        private const val LOG_TAG = "MusicService"
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Loading())

    private var mediaPlayer: MediaPlayer? = null
    private var songUrl = ""
    private val binder = PlayerServiceBinder()
    private var timerJob: Job? = null

    //region LIFECYCLE METHODS
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        songUrl = p0?.getStringExtra("song_url") ?: ""
        initMediaPlayer()
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

//endregion

    //region PRIVATE METHODS

    override fun getPlayerState(): StateFlow<PlayerState> {
        return _playerState.asStateFlow()
    }

    override fun startPlayer() {
        Log.d(LOG_TAG, "Media Player started")
        mediaPlayer?.start()
        startTimer()
    }

    override fun pausePlayer() {
        Log.d(LOG_TAG, "Media Player paused")

        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(300L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) {
            Log.d(LOG_TAG, "Media Player не создан потому что нет ссылки")
            return
        }
        Log.d(LOG_TAG, "Media Player инициализируется")
        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            Log.d(LOG_TAG, "Media Player prepared")
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            Log.d(LOG_TAG, "Playback completed")
            timerJob?.cancel().apply { null }
            _playerState.value = PlayerState.Prepared()
        }
    }

    private fun releasePlayer() {
        Log.d(LOG_TAG, "Media Player released")

        mediaPlayer?.stop()
        timerJob?.cancel()
        _playerState.value = PlayerState.Loading()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //endregion
}