package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val interactor: SearchInteractorInterface
) : ViewModel() {

    private var _uiState: MutableLiveData<Int> = MutableLiveData()
    val uiState: LiveData<Int> = _uiState


    fun getHistory(): List<Track> {
        return interactor.getHistory()
    }

    fun clearHistory() {
        interactor.clearHistory()
    }

    fun addTrackInHistory(track: Track) {
        interactor.addTrackInHistory(track)
    }

    fun searchAction(expression: String, consumer: SearchInteractorInterface.TracksConsumer) {
        interactor.searchAction(expression, consumer)
    }

}