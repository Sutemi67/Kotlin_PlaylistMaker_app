package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DatabaseInteractor(
    private val databaseRepository: DatabaseRepositoryInterface
) : DatabaseInteractorInterface {
    override suspend fun addTrackToFavourites(track: Track) {
        databaseRepository.addTrackToFavourites(track)
    }

    override suspend fun removeTrackFromFavourites(track: Track) {
        databaseRepository.deleteTrackFromFavourites(track)
    }

    override fun getFavouritesList(): Flow<List<Track>> {
        return databaseRepository.getFavouritesList()
    }

    override suspend fun addPlaylist(playlist: Playlist): Boolean {
        return databaseRepository.addPlaylist(playlist)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return databaseRepository.getAllPlaylists()
    }

    override suspend fun getPlaylistTracks(playlist: Playlist): Flow<List<Track>> {
        return databaseRepository.getPlaylistTracks(playlist)
    }

    override suspend fun removeTrackFromPlaylist(
        track: Track,
        playlist: Playlist
    ) {
        databaseRepository.removeTrackFromPlaylist(track, playlist)
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        databaseRepository.removePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        databaseRepository.updatePlaylist(playlist)
    }
}