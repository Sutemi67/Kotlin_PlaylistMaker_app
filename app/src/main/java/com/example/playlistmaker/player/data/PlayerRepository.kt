package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.domain.PlayerRepositoryInterface
import java.io.IOException

class PlayerRepository(
    private val player: MediaPlayer
) : PlayerRepositoryInterface {

    override fun setPlayer(
        previewUrl: String,
        onCompletion: () -> Unit
    ): PlaybackStatus {
        try {
            player.apply {
                setDataSource(previewUrl)
                prepareAsync()
                setOnCompletionListener {
                    onCompletion()
                }
                return PlaybackStatus.Ready
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("error", "поймал в setPlayer")
            return PlaybackStatus.Error
        }
    }

    override fun playOrPauseAction(): PlaybackStatus {
        if (player.isPlaying) {
            pause()
            return PlaybackStatus.Paused
        } else {
            play()
            return PlaybackStatus.Playing
        }
    }

    override fun playerGetCurrentTime(): Long = player.currentPosition.toLong()

    override fun stop(): PlaybackStatus.Ready {
        player.stop()
        return PlaybackStatus.Ready
    }

    override fun pause(): PlaybackStatus.Paused {
        player.pause()
        return PlaybackStatus.Paused
    }

    override fun reset() {
        player.reset()
    }

    override fun play(): PlaybackStatus.Playing {
        player.start()
        return PlaybackStatus.Playing
    }
}