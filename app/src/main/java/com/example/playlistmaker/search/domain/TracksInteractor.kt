package com.example.playlistmaker.search.domain

import android.content.Context
import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchActivitySearchAction(expression: String, consumer: TracksConsumer)
    fun settingsActivityShareAction(context: Context)
    fun settingsActivityOpenLinkAction(context: Context)
    fun settingsActivityAgreementAction(context: Context)

    interface TracksConsumer {
        fun consume(findTracks: List<Track>, response: Int)
    }
}