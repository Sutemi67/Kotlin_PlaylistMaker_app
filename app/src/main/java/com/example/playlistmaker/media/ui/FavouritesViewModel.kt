package com.example.playlistmaker.media.ui

import android.util.Log
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

    private val _favouriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favouriteTracks: StateFlow<List<Track>> = _favouriteTracks

    init {
        refreshFavourites()
    }

    fun refreshFavourites() {
        viewModelScope.launch {
            interactor.getFavouritesList().collect { _favouriteTracks.value = it }
        }
        Log.i("compose1", "refreshing favourites, ${favouriteTracks.value}")
    }

}