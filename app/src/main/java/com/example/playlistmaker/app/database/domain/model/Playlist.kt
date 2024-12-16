package com.example.playlistmaker.app.database.domain.model

import com.example.playlistmaker.search.domain.models.Track

data class Playlist(
    val id: Int?,
    val name: String,
    val description: String?,
    val coverUrl: String?,
    var tracks: List<Track>,
    val count: Int
)
