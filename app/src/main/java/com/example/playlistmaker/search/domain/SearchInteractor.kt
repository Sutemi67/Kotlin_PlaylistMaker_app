package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchInteractor(
    private val repository: SearchRepositoryInterface
) : SearchInteractorInterface {

    override suspend fun searchAction(
        expression: String
    ): Flow<TrackListAndResponse> {
        return repository.searchAction(expression)
    }

    override fun getPrefs(): SharedPreferences {
        return repository.getPrefs()
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun addTrackInHistory(track: Track) {
        repository.addTrackInHistory(track)
    }

    override fun saveHistory() {
        repository.saveHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}
