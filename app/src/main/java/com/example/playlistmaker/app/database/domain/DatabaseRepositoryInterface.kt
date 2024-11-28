package com.example.playlistmaker.app.database.domain

import com.example.playlistmaker.app.database.data.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepositoryInterface {
    fun addTrackToFavourites(track: Track)
    fun deleteTrackFromFavourites(track: TrackEntity)
    fun getFavouritesList(): Flow<List<Track>>
}