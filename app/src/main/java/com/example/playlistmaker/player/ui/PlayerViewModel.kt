package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import com.example.playlistmaker.player.domain.PlayerInteractorInterface
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactor: PlayerInteractorInterface,
    private val searchRepository: SearchRepositoryInterface
) : ViewModel() {

    private var playerControls: PlayerControlsInterface? = null

    private val _playerStateAsState = MutableStateFlow<PlayerState>(PlayerState.Loading())
    val playerStateAsState: StateFlow<PlayerState> = _playerStateAsState.asStateFlow()

    private val _addingStatus2 = MutableStateFlow<Boolean>(false)
    val addingStatus2: StateFlow<Boolean> = _addingStatus2.asStateFlow()

    fun setAudioPlayerControl(playerControl: PlayerControlsInterface) {
        playerControls = playerControl
        viewModelScope.launch {
            playerControls?.getPlayerState()?.collect {
                _playerStateAsState.value = it
            }
        }
    }

    fun onPlayerButtonClicked() {
        if (playerStateAsState.value is PlayerState.Playing) {
            playerControls?.pausePlayer()
        } else {
            playerControls?.startPlayer()
        }
    }

    fun removeAudioPlayerControl() {
        playerControls = null
    }

    override fun onCleared() {
        super.onCleared()
        playerControls = null
    }


    fun toggleFavourite(track: Track) {
        viewModelScope.launch {
            searchRepository.updateTrackFavouriteStatus(track, !track.isFavourite)
        }
        searchRepository.addTrackInHistory(
            track.copy(isFavourite = !track.isFavourite)
        )
    }

    fun addInPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            _addingStatus2.value = interactor.addTrackInPlaylist(track, playlist)
            Log.i("compose", "статус добавления трека ${addingStatus2.value}")
        }
    }
}