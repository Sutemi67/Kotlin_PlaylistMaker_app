package com.example.playlistmaker.app.database.data

import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepository(
    private val database: TracksDb,
    private val converter: TracksConverter
) : DatabaseRepositoryInterface {
    override fun addTrackToFavourites(track: Track) {
        database.tracksDbDao().insertTrack(converter.mapToTrackEntity(track))
    }

    override fun deleteTrackFromFavourites(track: TrackEntity) {
        database.tracksDbDao().deleteTrack(track)
    }

    override fun getFavouritesList(): Flow<List<Track>> = flow {
        val tracks = database.tracksDbDao().getAllTracks()
        emit(converter.mapToListOfTracks(tracks))
    }
}