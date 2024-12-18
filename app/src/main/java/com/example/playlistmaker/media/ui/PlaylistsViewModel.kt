package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface

class PlaylistsViewModel(
    private val interactor: DatabaseInteractorInterface
) : ViewModel() {

    private val _listState = MutableLiveData<PlaylistState>(PlaylistState.EmptyList)
    val listState: LiveData<PlaylistState> = _listState

    suspend fun getPlaylists() {
        interactor
            .getAllPlaylists()
            .collect {
                if (it.isEmpty()) {
                    _listState.value = PlaylistState.EmptyList
                } else {
                    _listState.value = PlaylistState.FullList(it)
                }
            }
    }

}