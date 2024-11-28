package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DatabaseInteractor(
    private val databaseRepository: DatabaseRepositoryInterface
) : DatabaseInteractorInterface {
    override fun addTrackToFavourites(track: Track) {
        databaseRepository.addTrackToFavourites(track)
    }

    override fun deleteTrackFromFavourites() {
        TODO("Not yet implemented")
    }

    override fun getFavouritesList(): Flow<List<Track>> {
        return databaseRepository.getFavouritesList()
    }
}