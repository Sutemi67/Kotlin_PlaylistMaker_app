package com.example.playlistmaker.media.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.MediaInteractorInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val interactor: MediaInteractorInterface
) : ViewModel() {

    var count = MutableLiveData(0)

    private val _favouriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favouriteTracks: StateFlow<List<Track>> = _favouriteTracks

    fun refreshFavourites() {
        viewModelScope.launch {
            interactor.getFavouritesList().collect { _favouriteTracks.value = it }
        }
    }

//    fun getTracksCount() {
//        viewModelScope.launch(Dispatchers.Default) {
//            count.postValue(interactor.getTracksCount())
//        }
//    }
}