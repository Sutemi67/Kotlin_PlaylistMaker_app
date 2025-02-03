package com.example.playlistmaker.player.domain

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractor(
    private val repository: PlayerRepositoryInterface
) : PlayerInteractorInterface {

//    override fun setPlayer(
//        previewUrl: String,
//        context: Context,
//        state: (PlaybackStatus) -> Unit,
//        onCompletion: () -> Unit
//    ) {
//        state(repository.setPlayer(previewUrl) { onCompletion() })
//    }
//
//    override fun pause() {
//        repository.pause()
//    }
//
//    override fun reset() {
//        repository.reset()
//    }
//
//    override fun playerGetCurrentTime(): Long = repository.playerGetCurrentTime()
//
//    override fun playOrPauseAction(): PlaybackStatus {
//        return repository.playOrPauseAction()
//    }

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return repository.addTrackInPlaylist(track, playlist)
    }

}