package com.example.playlistmaker.player.ui

import com.example.playlistmaker.app.database.domain.model.Playlist

data class TrackToPlaylistState(
    val playlist: Playlist,
    val state: Boolean
)