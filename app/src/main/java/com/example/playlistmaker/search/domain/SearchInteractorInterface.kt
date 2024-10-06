package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track

interface SearchInteractorInterface {
    fun searchAction(expression: String, consumer: TracksConsumer)
    fun getPrefs(): SharedPreferences
    fun getHistory(): List<Track>
    fun addTrackInHistory(track: Track)
    fun saveHistory()
    fun clearHistory()
}