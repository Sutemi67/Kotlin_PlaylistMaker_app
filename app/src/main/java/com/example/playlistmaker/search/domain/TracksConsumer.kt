package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksConsumer {
    fun consume(findTracks: List<Track>, response: Int)
}