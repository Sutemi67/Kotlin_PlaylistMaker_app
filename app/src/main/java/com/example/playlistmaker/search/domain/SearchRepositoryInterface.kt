package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.domain.models.Track

interface SearchRepositoryInterface {
    fun searchAction(expression: String): TrackListAndResponse
    fun getPrefs(): SharedPreferences
    fun getHistory(): List<Track>
    fun addTrackInHistory(track: Track)
    fun saveHistory()
    fun clearHistory()
}