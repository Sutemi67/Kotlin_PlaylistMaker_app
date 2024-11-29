package com.example.playlistmaker.app.database.data

import android.util.Log
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepository(
    private val databaseOfTracks: DatabaseOfTracks,
    private val converter: TracksConverter
) : DatabaseRepositoryInterface {
    override suspend fun addTrackToFavourites(track: Track) {
        val trackEntity = converter.mapToTrackEntity(track)
        Log.e("DATABASE", "Inserting track with ID: ${trackEntity.trackId}")
        databaseOfTracks.tracksDbDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrackFromFavourites(track: Track) {
        val trackEntity = converter.mapToTrackEntity(track)
        databaseOfTracks.tracksDbDao().removeTrack(trackEntity)
        Log.e("DATABASE", "Removing track with ID: ${track.trackId}")
    }

    override fun getFavouritesList(): Flow<List<Track>> = flow {
        val tracks = databaseOfTracks.tracksDbDao().getAllTracks()
        emit(converter.mapToListOfTracks(tracks))
    }

    override fun getTracksCount(): Int {
        return databaseOfTracks.tracksDbDao().getTracksCount()
    }
}