package com.example.playlistmaker.player.ui

import com.example.playlistmaker.app.database.domain.model.Playlist

interface AddingTrackInPlaylistInterface {
    fun addTrackInPlaylist(playlist: Playlist)
}