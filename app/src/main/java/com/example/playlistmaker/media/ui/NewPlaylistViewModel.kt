package com.example.playlistmaker.media.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.app.database.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: DatabaseInteractorInterface
) : ViewModel() {

//    private val _addingState = MutableLiveData(false)
//    val addingState: LiveData<Boolean> = _addingState

    fun addPlaylist(
        name: String,
        description: String,
        image: String?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val result = interactor.addPlaylist(
                playlist = Playlist(
                    id = null,
                    name = name,
                    description = description,
                    tracks = emptyList(),
                    count = 0,
                    coverUrl = image
                )
            )
            onResult(result)
        }
    }

}
