package com.example.playlistmaker.player.data

import android.util.Log
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.player.domain.PlayerRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track

class PlayerRepository(
    private val database: DatabaseRepositoryInterface
) : PlayerRepositoryInterface {

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        if (playlist.tracks.contains(track)) {
            Log.e("DATABASE", "трек уже есть в базе")
            return false
        } else {
            val list = playlist.tracks.toMutableList()
            list.add(track)
            playlist.tracks = list.toList()
            database.updatePlaylist(playlist)
            Log.d("DATABASE", "добавил трек в базу, плейлист такой - ${playlist.tracks}")
            return true
        }
    }
}