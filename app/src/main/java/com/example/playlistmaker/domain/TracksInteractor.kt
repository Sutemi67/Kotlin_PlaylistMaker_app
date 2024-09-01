package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun doRequest(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(findTracks: List<Track>)
    }
}