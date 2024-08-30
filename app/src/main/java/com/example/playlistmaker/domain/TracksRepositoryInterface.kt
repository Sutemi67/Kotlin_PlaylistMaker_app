package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface TracksRepositoryInterface {
    fun searchTracks(expression: String): List<Track>
}