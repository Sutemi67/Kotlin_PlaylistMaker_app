package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

data class TrackListAndResponse(val trackList: List<Track>, val responseCode: Int)
