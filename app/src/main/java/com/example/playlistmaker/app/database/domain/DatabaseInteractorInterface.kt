package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseInteractorInterface {
    suspend fun addTrackToFavourites(track: Track)
    suspend fun removeTrackFromFavourites(track: Track)
    fun getFavouritesList(): Flow<List<Track>>
}