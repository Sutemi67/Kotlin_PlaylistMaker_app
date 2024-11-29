package com.example.playlistmaker.app.database.data

import android.util.Log
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepository(
    private val database: TracksDb,
    private val converter: TracksConverter
) : DatabaseRepositoryInterface {

    override suspend fun addTrackToFavourites(track: Track) {
        val trackEntity = converter.mapToTrackEntity(track)
        Log.e("DATABASE", "Inserting track with ID: ${trackEntity.trackId}")
        database.tracksDbDao().insertTrack(trackEntity)
    }

    override fun getFavouritesList(): Flow<List<Track>> = flow {
        val tracks = database.tracksDbDao().getAllTracks()
        emit(converter.mapToListOfTracks(tracks))
    }

    override suspend fun deleteTrackFromFavourites(track: Track) {
        database.tracksDbDao().deleteTrack(converter.mapToTrackEntity(track))
    }

    override fun getTracksCount(): Int {
        return database.tracksDbDao().getTracksCount()
    }
}