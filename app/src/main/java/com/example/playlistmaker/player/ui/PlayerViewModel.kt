package com.example.playlistmaker.player.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.media.ui.PlaylistState
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.player.domain.PlayerInteractorInterface
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val interactor: PlayerInteractorInterface,
    private val databaseInteractor: DatabaseInteractorInterface,
    private val searchRepository: SearchRepositoryInterface
) : ViewModel() {

    private var _playbackStatus = MutableLiveData<PlaybackStatus>(PlaybackStatus.Ready)
    fun getPlaybackLiveData(): LiveData<PlaybackStatus> = _playbackStatus
    private var _counterData = MutableLiveData("")
    fun getCounterText(): LiveData<String> = _counterData
    private var _likeState = MutableLiveData(false)
    fun getLikeState(): LiveData<Boolean> = _likeState
    private val _listState = MutableLiveData<PlaylistState>(PlaylistState.EmptyList)
    val listState: LiveData<PlaylistState> = _listState
    private var timerJob: Job? = null

    fun setPlayer(
        previewUrl: String,
        context: Context,
    ) {
        interactor.setPlayer(
            previewUrl = previewUrl,
            context = context,
            state = { state -> _playbackStatus.postValue(state) },
            onCompletion = {
                timerJob?.cancel()
                _playbackStatus.value = PlaybackStatus.Ready
            }
        )
    }

    fun playOrPauseAction() {
        _playbackStatus.value = interactor.playOrPauseAction()
        if (getPlaybackLiveData().value == PlaybackStatus.Playing) {
            if (timerJob == null || timerJob?.isCancelled == true)
                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(300L)
                        _counterData.postValue(
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                                playerGetCurrentTime()
                            )
                        )
                    }
                }
        } else {
            timerJob?.cancel()
        }
    }

    fun pausing() = interactor.pause()
    fun reset() = interactor.reset()
    private fun playerGetCurrentTime(): Long = interactor.playerGetCurrentTime()

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

    fun addInPlaylist(track: Track) {
        interactor.addTrackInPlaylist(track)
    }
}