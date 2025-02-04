package com.example.playlistmaker.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
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
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 1

    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Loading())
    private var mediaPlayer: MediaPlayer? = null
    private var songUrl = ""
    private var artistName = ""
    private var trackName = ""
    private val binder = PlayerServiceBinder()
    private var timerJob: Job? = null


    //region LIFECYCLE METHODS
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        songUrl = p0?.getStringExtra("song_url") ?: ""
        artistName = p0?.getStringExtra("artist_name") ?: "Unknown artist"
        trackName = p0?.getStringExtra("track_name") ?: "Unknown track"
        initMediaPlayer()
        return binder
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

//endregion

    //region PRIVATE METHODS

    private fun createNotificationChannel() {
        // Создание каналов доступно только с Android 8.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSilent(true)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return _playerState.asStateFlow()
    }

    override fun startForeground() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun stopForeground() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.e(LOG_TAG, "stop self")
    }

    override fun startPlayer() {
        Log.d(LOG_TAG, "Media Player started")
        mediaPlayer?.start()
        startTimer()
        startForeground()
    }

    override fun pausePlayer() {
        Log.d(LOG_TAG, "Media Player paused")

        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
        stopForeground()
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
            stopForeground()
        }
    }

    private fun releasePlayer() {
        Log.d(LOG_TAG, "Media Player released")
        stopForeground()
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