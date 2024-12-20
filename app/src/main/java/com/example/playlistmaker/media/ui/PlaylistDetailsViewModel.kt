package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.media.ui.stateInterfaces.TrackListState

class PlaylistDetailsViewModel(
    private val interactor: DatabaseInteractorInterface
) : ViewModel() {

    private val _listState = MutableLiveData<TrackListState>(TrackListState.Empty)
    val listState: LiveData<TrackListState> = _listState

    suspend fun getPlaylistTracks(playlist: Playlist) {
        interactor.getPlaylistTracks(playlist).collect {
            if (it.isEmpty()) {
                _listState.value = TrackListState.Empty
            } else {
                _listState.value = TrackListState.Filled(it)
            }
        }

    }
}