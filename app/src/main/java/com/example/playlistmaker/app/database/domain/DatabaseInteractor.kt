package com.example.playlistmaker.app.database.domain

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
}