package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepositoryInterface {
    suspend fun addTrackToFavourites(track: Track)
    suspend fun deleteTrackFromFavourites(track: Track)
    fun getFavouritesList(): Flow<List<Track>>
    fun getTracksCount(): Int
}