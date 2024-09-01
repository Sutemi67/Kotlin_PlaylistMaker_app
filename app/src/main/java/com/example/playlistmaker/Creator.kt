package com.example.playlistmaker

import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.NetworkClientImpl
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.domain.TracksInteractorImpl

object Creator {

    fun provideTracksInteractorImpl(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(NetworkClientImpl())
    }
}