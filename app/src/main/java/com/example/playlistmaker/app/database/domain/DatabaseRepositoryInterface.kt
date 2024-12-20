package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepositoryInterface {

    //favourite tracks
    suspend fun addTrackToFavourites(track: Track)
    suspend fun deleteTrackFromFavourites(track: Track)
    fun getFavouritesList(): Flow<List<Track>>

    //playlists
    suspend fun addPlaylist(playlist: Playlist): Boolean
    suspend fun removePlaylist(playlist: Playlist)
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistTracks(playlist: Playlist): Flow<List<Track>>
}