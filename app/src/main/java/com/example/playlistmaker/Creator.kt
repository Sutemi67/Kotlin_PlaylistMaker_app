package com.example.playlistmaker

import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TracksInteractorInterface
import com.example.playlistmaker.domain.TracksRepositoryInterface
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    fun provideTracksInteractorImpl(): TracksInteractorInterface {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): TracksRepositoryInterface {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
}