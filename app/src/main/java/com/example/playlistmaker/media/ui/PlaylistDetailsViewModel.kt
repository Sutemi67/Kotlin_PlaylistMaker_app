package com.example.playlistmaker.media.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.media.ui.stateInterfaces.TrackListState
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailsViewModel(
    private val interactor: DatabaseInteractorInterface
) : ViewModel() {

    private val _listState = MutableLiveData<TrackListState>(TrackListState.Empty(emptyList()))
    val listState: LiveData<TrackListState> = _listState

    suspend fun getPlaylistTracks(playlist: Playlist) {
        interactor.getPlaylistTracks(playlist).collect {
            if (it.isEmpty()) {
                _listState.value = TrackListState.Empty(emptyList())
            } else {
                _listState.value = TrackListState.Filled(it)
            }
        }
    }

    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
        interactor.removeTrackFromPlaylist(track, playlist)
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.removePlaylist(playlist)
        }
    }

    fun onShareClick(context: Context, playlist: Playlist) {
        val intent = Intent(Intent.ACTION_SEND)
        var tracks = ""
        var counter = 0
        val size = playlist.tracks.size
        val russianTracksCount = when (size % 10) {
            1 -> "$size трек"
            in 2..4 -> "$size трека"
            else -> "$size треков"
        }
        val formatter = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
        for (i in playlist.tracks) {
            counter++
            tracks += "$counter. ${i.artistName} - ${i.trackName} (${formatter.format(i.trackTime)})\n"
        }
        val text =
            "Название плейлиста: ${playlist.name}\n" +
                    "Описание: ${playlist.description}\n" +
                    "$russianTracksCount: \n" +
                    tracks
        Log.i("log", text)
        intent.setType("text/plain")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            text
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, intent, null)
    }

    fun showNoTracksDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("В плейлисте отсутствуют треки")
            .setPositiveButton("Ok") { dialog, witch ->
                //
            }
            .show()
    }

}