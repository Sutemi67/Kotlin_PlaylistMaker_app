package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.SEARCH_UI_STATE_FILLED
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOCONNECTION
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOTHINGFOUND
import com.example.playlistmaker.app.SEARCH_UI_STATE_PROGRESS
import com.example.playlistmaker.search.data.TrackAdapter
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val interactor: SearchInteractorInterface,
    private val adapter: TrackAdapter
) : ViewModel() {

    private var _uiState: MutableLiveData<Int> = MutableLiveData(SEARCH_UI_STATE_FILLED)
    val uiState: LiveData<Int> = _uiState
    private var _historyState = MutableLiveData(true)
    val historyState: LiveData<Boolean> = _historyState

    fun setAdapterList(list: List<Track>) {
        adapter.setData(list)
    }

    fun getAdapter() = adapter

    fun getHistory(): List<Track> {
        val result = interactor.getHistory()
        _historyState.postValue(result.isEmpty())
        return result
    }

    fun clearHistory() {
        interactor.clearHistory()
        _historyState.postValue(true)
    }

    fun addTrackInHistory(track: Track) {
        interactor.addTrackInHistory(track)
    }

    fun searchAction(expression: String, consumer: SearchInteractorInterface.TracksConsumer) {
        interactor.searchAction(expression, consumer)
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