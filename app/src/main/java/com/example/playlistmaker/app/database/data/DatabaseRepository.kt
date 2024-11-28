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
        database.tracksDbDao().insertTrack(converter.mapToTrackEntity(track))
        Log.e("DATABASE", "track added to database")
    }

    override suspend fun deleteTrackFromFavourites(track: Track) {
        database.tracksDbDao().deleteTrack(converter.mapToTrackEntity(track))
    }

    override fun getFavouritesList(): Flow<List<Track>> = flow {
        val tracks = database.tracksDbDao().getAllTracks()
        emit(converter.mapToListOfTracks(tracks))
    }
}