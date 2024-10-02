package com.example.playlistmaker.player.data

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.domain.PlayerRepositoryInterface
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException

class PlayerRepository : PlayerRepositoryInterface, KoinComponent {

    private val player: MediaPlayer by inject()
    override fun player(): MediaPlayer = player

    override fun playOrPauseAction(): PlaybackStatus {
        if (player.isPlaying) {
            pause()
            return PlaybackStatus.Paused
        } else {
            play()
            return PlaybackStatus.Playing
        }
    }

    override fun setPlayer(
        previewUrl: String,
        context: Context
    ): PlaybackStatus {
        try {
            player.apply {
                setDataSource(previewUrl)
                prepareAsync()
                return PlaybackStatus.Ready
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("error", "поймал в setPlayer")
            return PlaybackStatus.Error
        }
    }

    override fun stopping(): PlaybackStatus.Ready {
        player.stop()
        return PlaybackStatus.Ready
    }

    override fun pause(): PlaybackStatus.Paused {
        player.pause()
        return PlaybackStatus.Paused
    }

    override fun release() {
        player.release()
    }

    override fun play(): PlaybackStatus.Playing {
        player.start()
        return PlaybackStatus.Playing
    }
}