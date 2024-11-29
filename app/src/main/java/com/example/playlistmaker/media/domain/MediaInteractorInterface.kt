package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaInteractorInterface {
    fun getFavouritesList(): Flow<List<Track>>
    fun getTracksCount(): Int
}