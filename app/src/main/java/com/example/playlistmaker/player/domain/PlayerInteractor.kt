package com.example.playlistmaker.player.domain

import android.content.Context
import com.example.playlistmaker.player.data.PlaybackStatus

class PlayerInteractor(
    private val repository: PlayerRepositoryInterface
) : PlayerInteractorInterface {

    override fun setPlayer(
        previewUrl: String,
        context: Context,
        state: (PlaybackStatus) -> Unit,
        onCompletion: () -> Unit
    ) {
        state(repository.setPlayer(previewUrl) { onCompletion() })
    }

    override fun pause() {
        repository.pause()
    }

    override fun reset() {
        repository.reset()
    }

    override fun playerGetCurrentTime(): Long = repository.playerGetCurrentTime()

    override fun playOrPauseAction(): PlaybackStatus {
        return repository.playOrPauseAction()
    }

}