package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.TrackListAndResponse

interface TracksRepository {
    fun refillTrackList(expression: String): TrackListAndResponse
}