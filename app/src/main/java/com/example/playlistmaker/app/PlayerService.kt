package com.example.playlistmaker.app

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class PlayerService : Service() {

    companion object {
        private const val LOG_TAG = "MusicService"
    }

    private var mediaPlayer: MediaPlayer? = null
    private var songUrl = ""
    private val binder = MusicServiceBinder()

    inner class MusicServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            Log.d(LOG_TAG, "Media Player prepared")
        }
        mediaPlayer?.setOnCompletionListener {
            Log.d(LOG_TAG, "Playback completed")
        }
    }

    fun startPlayer() {
        mediaPlayer?.start()
    }

    fun pausePlayer() {
        mediaPlayer?.pause()
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }
}