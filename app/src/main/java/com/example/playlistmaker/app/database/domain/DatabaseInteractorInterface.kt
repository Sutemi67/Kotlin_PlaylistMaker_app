package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseInteractorInterface {
    fun addTrackToFavourites()
    fun deleteTrackFromFavourites()
    fun getFavouritesList(): Flow<List<Track>>
}