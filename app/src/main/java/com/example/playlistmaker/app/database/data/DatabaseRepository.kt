package com.example.playlistmaker.app.database.data

import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepository(
    private val database: TracksDb,
    private val converter: TracksConverter
) : DatabaseRepositoryInterface {
    override fun addTrackToFavourites() {

    }

    override fun deleteTrackFromFavourites() {
        TODO("Not yet implemented")
    }

    override fun getFavouritesList(): Flow<List<Track>> = flow {
        val tracks = database.tracksDbDao().getAllTracks()
        emit(convertTrack(tracks))
    }

    private fun convertTrack(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }
}