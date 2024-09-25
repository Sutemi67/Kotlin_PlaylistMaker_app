package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.TrackListAndResponse

interface TracksRepositoryInterface {
    fun refillTrackList(expression: String): TrackListAndResponse
}