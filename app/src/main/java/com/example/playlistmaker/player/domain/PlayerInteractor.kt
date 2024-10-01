package com.example.playlistmaker.player.domain

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlaybackStatus

class PlayerInteractor(
    private val repository: PlayerRepositoryInterface
) : PlayerInteractorInterface {

    override fun setPlayer(
        previewUrl: String,
        context: Context,
        state: (PlaybackStatus) -> Unit
    ) {
        state(repository.setPlayer(previewUrl, context))
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }

    override fun playOrPauseAction(): PlaybackStatus {
        return repository.playOrPauseAction()
    }

    override fun player(): MediaPlayer = repository.player()

    override fun stopping(): PlaybackStatus.Ready {
        return repository.stopping()
    }
}