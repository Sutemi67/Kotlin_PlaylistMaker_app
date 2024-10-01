package com.example.playlistmaker.player.data

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.PlayerRepositoryInterface
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException

class PlayerRepository(
) : PlayerRepositoryInterface, KoinComponent {

    val player: MediaPlayer by inject()

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

    override fun pause(): PlaybackStatus.Paused {
        player.pause()
        return PlaybackStatus.Paused
    }

    override fun release() {
        player.release()
    }

    override fun setPlayer(
        previewUrl: String,
        context: Context
    ): PlaybackStatus {
        player.apply {
            try {
                setDataSource(previewUrl)
                prepareAsync()
                return PlaybackStatus.Ready
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    R.string.player_error_loading_preview,
                    Toast.LENGTH_SHORT
                ).show()
                return PlaybackStatus.Error
            }
        }
    }

    override fun stopping(): PlaybackStatus.Ready {
        player.stop()
        return PlaybackStatus.Ready
    }

    override fun play(): PlaybackStatus.Playing {
        player.start()
        return PlaybackStatus.Playing
    }
}