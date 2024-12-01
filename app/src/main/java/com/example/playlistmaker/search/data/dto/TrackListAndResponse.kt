package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.domain.models.Track

data class TrackListAndResponse(
    val trackList: List<Track>,
    val responseCode: Int
)



