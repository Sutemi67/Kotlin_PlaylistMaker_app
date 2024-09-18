package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackListAndResponse
import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
//    fun refillTrackList(expression: String): List<Track>
    fun refillTrackList(expression: String): TrackListAndResponse
}