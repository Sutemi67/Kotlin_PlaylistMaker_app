package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksInteractorInterface {
    fun doRequest(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(findTracks: List<Track>)
    }
}