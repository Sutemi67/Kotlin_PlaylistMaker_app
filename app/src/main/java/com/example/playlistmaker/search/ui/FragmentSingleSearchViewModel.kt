package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.SEARCH_UI_STATE_FILLED
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOCONNECTION
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOTHINGFOUND
import com.example.playlistmaker.app.SEARCH_UI_STATE_PROGRESS
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FragmentSingleSearchViewModel(
    private val interactor: SearchInteractorInterface,
) : ViewModel() {
    private var _uiState: MutableLiveData<Int> = MutableLiveData(SEARCH_UI_STATE_FILLED)
    val uiState: LiveData<Int> = _uiState
    private var _isHistoryEmpty = MutableLiveData(true)
    val isHistoryEmpty: LiveData<Boolean> = _isHistoryEmpty

    fun getHistory(): List<Track> {
        val result = interactor.getHistory()
        _isHistoryEmpty.postValue(result.isEmpty())
        return result
    }

    fun clearHistory() {
        interactor.clearHistory()
        _isHistoryEmpty.postValue(true)
    }

    fun addTrackInHistory(track: Track) {
        interactor.addTrackInHistory(track)
    }

    suspend fun searchAction(expression: String): Flow<TrackListAndResponse> {
        return interactor.searchAction(expression)
    }

    fun setUIState(state: Int) {
        when (state) {
            SEARCH_UI_STATE_FILLED -> _uiState.postValue(SEARCH_UI_STATE_FILLED)
            SEARCH_UI_STATE_NOTHINGFOUND -> _uiState.postValue(SEARCH_UI_STATE_NOTHINGFOUND)
            SEARCH_UI_STATE_NOCONNECTION -> _uiState.postValue(SEARCH_UI_STATE_NOCONNECTION)
            SEARCH_UI_STATE_PROGRESS -> _uiState.postValue(SEARCH_UI_STATE_PROGRESS)
        }
    }
}