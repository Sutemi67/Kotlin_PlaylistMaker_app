package com.example.playlistmaker.media.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistsViewModel(
    private val interactor: DatabaseInteractorInterface
) : ViewModel() {

//    private val _listState = MutableLiveData<PlaylistState>(PlaylistState.EmptyList)
//    val listState: LiveData<PlaylistState> = _listState

    private val _playlistState = MutableStateFlow<PlaylistState>(PlaylistState.EmptyList)
    val playlistState: StateFlow<PlaylistState> = _playlistState.asStateFlow()

    suspend fun getPlaylists() {
        interactor
            .getAllPlaylists()
            .collect {
                if (it.isEmpty()) {
                    _playlistState.value = PlaylistState.EmptyList
                } else {
                    _playlistState.value = PlaylistState.FullList(it)
                }
            }
    }

}