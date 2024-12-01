package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.SEARCH_UI_STATE_FILLED
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOCONNECTION
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOTHINGFOUND
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(
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

    suspend fun searchAction(expression: String): List<Track> {
        var tl: List<Track> = emptyList()
        interactor
            .searchAction(expression)
            .collect {
                if (it.trackList.isEmpty()) {
                    if (it.responseCode == 400) {
                        Log.e("ee", "400, no connection")
                        setUIState(SEARCH_UI_STATE_NOCONNECTION)
                        return@collect
                    }
                    Log.e("ee", "${it.responseCode}, nothing found")
                    setUIState(SEARCH_UI_STATE_NOTHINGFOUND)
                } else {
                    tl = it.trackList
                    setUIState(SEARCH_UI_STATE_FILLED)
                    Log.e("ee", "${it.responseCode}, full list: $tl")
                }
            }
        return tl
    }

    private fun setUIState(state: Int) = _uiState.postValue(state)

}