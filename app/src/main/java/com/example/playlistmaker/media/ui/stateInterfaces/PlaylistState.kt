package com.example.playlistmaker.media.ui.stateInterfaces

import com.example.playlistmaker.app.database.domain.model.Playlist

sealed interface PlaylistState {
    data object EmptyList : PlaylistState
    data class FullList(val playlist: List<Playlist>) : PlaylistState
}