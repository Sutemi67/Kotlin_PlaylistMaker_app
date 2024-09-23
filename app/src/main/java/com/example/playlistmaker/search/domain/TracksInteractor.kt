package com.example.playlistmaker.search.domain

import android.content.Context
import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun doRequest(expression: String, consumer: TracksConsumer)
    fun shareClick(context: Context)

    interface TracksConsumer {
        fun consume(findTracks: List<Track>, response: Int)
    }
}