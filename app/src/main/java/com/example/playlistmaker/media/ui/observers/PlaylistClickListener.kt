package com.example.playlistmaker.media.ui.observers

import com.example.playlistmaker.app.database.domain.model.Playlist

interface PlaylistClickListener {
    fun onClick(playlist: Playlist)
}