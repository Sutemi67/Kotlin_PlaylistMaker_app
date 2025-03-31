package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(
    private val interactor: SearchInteractorInterface,
) : ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(UiState.FullSearch)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getHistory(): List<Track> {
        val result = interactor.getHistory()
        _uiState.value = if (result.isEmpty()) {
            UiState.EmptyHistory
        } else {
            UiState.FullHistory
        }
        return result
    }

    fun clearHistory() {
        interactor.clearHistory()
        _uiState.value = UiState.EmptyHistory
    }

    fun addTrackInHistory(track: Track) {
        interactor.addTrackInHistory(track)
    }

    suspend fun searchAction(expression: String): List<Track> {
        var tl: List<Track> = emptyList()
        _uiState.value = UiState.Loading
        interactor
            .searchAction(expression)
            .collect {
                if (it.trackList.isEmpty()) {
                    if (it.responseCode == 400) {
                        Log.e("ee", "400, no connection")
                        _uiState.value = UiState.NoConnection
                        return@collect
                    }
                    Log.e("ee", "${it.responseCode}, nothing found")
                    _uiState.value = UiState.NothingFound
                } else {
                    tl = it.trackList
                    _uiState.value = UiState.FullSearch
                    Log.e("ee", "${it.responseCode}, full list: $tl")
                }
            }
        return tl
    }
}

sealed interface UiState {
    object FullSearch : UiState
    object FullHistory : UiState
    object NothingFound : UiState
    object NoConnection : UiState
    object Loading : UiState
    object EmptyHistory : UiState
}