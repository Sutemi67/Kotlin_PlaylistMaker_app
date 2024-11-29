package com.example.playlistmaker.media.domain

import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class MediaInteractor(
    private val repository: DatabaseRepositoryInterface
) : MediaInteractorInterface {
    override fun getFavouritesList(): Flow<List<Track>> = repository.getFavouritesList()
    override fun getTracksCount(): Int = repository.getTracksCount()
}