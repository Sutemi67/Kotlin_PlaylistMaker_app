package com.example.playlistmaker.app.database.data

import android.util.Log
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepository(
    private val databaseMain: DatabaseMain,
    private val converter: DatabaseConverter
) : DatabaseRepositoryInterface {

    override suspend fun addTrackToFavourites(track: Track) {
        val trackEntity = converter.mapToTrackEntity(track)
        Log.e("DATABASE", "Inserting track with ID: ${trackEntity.trackId}")
        databaseMain.tracksDbDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrackFromFavourites(track: Track) {
        val trackEntity = converter.mapToTrackEntity(track)
        databaseMain.tracksDbDao().removeTrack(trackEntity)
        Log.e("DATABASE", "Removing track with ID: ${track.trackId}")
    }

    override fun getFavouritesList(): Flow<List<Track>> = flow {
        val tracks = databaseMain.tracksDbDao().getAllTracks()
        emit(converter.mapToListOfTracks(tracks))
    }

    override suspend fun addPlaylist(playlist: Playlist): Boolean {
        return try {
            val playlistEntity = converter.mapToPlaylistEntity(playlist)
            databaseMain.playlistsDao().createPlaylist(playlistEntity)
            true
        } catch (e: Exception) {
            Log.e("DATABASE", "playlist exists")
            false
        }
    }

    override suspend fun removePlaylist() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = databaseMain.playlistsDao().getPlaylists()
        emit(converter.mapToPlaylist(playlists))
    }
}