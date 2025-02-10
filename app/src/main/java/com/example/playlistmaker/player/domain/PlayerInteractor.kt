package com.example.playlistmaker.player.domain

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractor(
    private val repository: PlayerRepositoryInterface
) : PlayerInteractorInterface {

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return repository.addTrackInPlaylist(track, playlist)
    }

}