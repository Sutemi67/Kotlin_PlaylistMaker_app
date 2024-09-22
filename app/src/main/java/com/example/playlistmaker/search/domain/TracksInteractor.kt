package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun doRequest(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(findTracks: List<Track>, response: Int)
    }
}