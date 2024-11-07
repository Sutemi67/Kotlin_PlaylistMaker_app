package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractorInterface {
    suspend fun searchAction(expression: String): Flow<TrackListAndResponse>
    fun getPrefs(): SharedPreferences
    fun getHistory(): List<Track>
    fun addTrackInHistory(track: Track)
    fun saveHistory()
    fun clearHistory()
}