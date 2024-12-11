package com.example.playlistmaker.app.database.domain.model

data class Playlist(
    val name: String,
    val description: String?,
    val coverUrl: String?,
    val tracks: String,
    val count: Int
)
