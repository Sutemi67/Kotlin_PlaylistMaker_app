package com.example.playlistmaker.player.domain

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

interface PlayerRepositoryInterface {
    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean
}