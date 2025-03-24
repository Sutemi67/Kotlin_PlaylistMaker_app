package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import com.example.playlistmaker.player.domain.PlayerInteractorInterface
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val interactor: PlayerInteractorInterface,
    private val databaseInteractor: DatabaseInteractorInterface,
    private val searchRepository: SearchRepositoryInterface
) : ViewModel() {

    private var _likeState = MutableLiveData(false)
    fun getLikeState(): LiveData<Boolean> = _likeState
    private val _listState = MutableLiveData<PlaylistState>(PlaylistState.EmptyList)
    val listState: LiveData<PlaylistState> = _listState
    private val _addingStatus = MutableLiveData<TrackToPlaylistState>()
    val addingStatus: LiveData<TrackToPlaylistState> = _addingStatus

    private var playerControls: PlayerControlsInterface? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Loading())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    //for compose
    private val _playerStateAsState = MutableStateFlow<PlayerState>(PlayerState.Loading())
    val playerStateAsState: StateFlow<PlayerState> = _playerStateAsState.asStateFlow()

    private val _addingStatus2 = MutableStateFlow<Boolean>(false)
    val addingStatus2: StateFlow<Boolean> = _addingStatus2.asStateFlow()

    fun setAudioPlayerControl(playerControl: PlayerControlsInterface) {
        playerControls = playerControl
        viewModelScope.launch {
            playerControls?.getPlayerState()?.collect {
                playerState.postValue(it)
                _playerStateAsState.value = it
            }
        }
    }

    fun onPlayerButtonClicked() {
        if (playerState.value is PlayerState.Playing) {
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

    fun setFavouriteState(track: Track) = if (track.isFavourite) _likeState.value = true else false

    fun toggleFavourite(track: Track) {
        viewModelScope.launch {
            _likeState.value = !track.isFavourite
            searchRepository.updateTrackFavouriteStatus(track, !track.isFavourite)
        }
        searchRepository.addTrackInHistory(
            track.copy(isFavourite = !track.isFavourite)
        )
    }

    suspend fun getPlaylists() {
        databaseInteractor
            .getAllPlaylists()
            .collect {
                if (it.isEmpty()) {
                    _listState.value = PlaylistState.EmptyList
                } else {
                    _listState.value = PlaylistState.FullList(it)
                }
            }
    }

    fun addInPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            _addingStatus.value = TrackToPlaylistState(
                playlist,
                interactor.addTrackInPlaylist(track, playlist)
            )
            _addingStatus2.value = interactor.addTrackInPlaylist(track, playlist)
            Log.i("compose", "статус добавления трека ${addingStatus2.value}")
        }
    }

    fun startForegroundPlaying() {
        if (playerState.value is PlayerState.Playing) {
            playerControls?.startForeground()
        }
    }

    fun stopForegroundPlaying() {
        playerControls?.stopForeground()
    }
}