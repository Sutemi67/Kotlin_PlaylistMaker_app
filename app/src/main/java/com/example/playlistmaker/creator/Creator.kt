package com.example.playlistmaker.creator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.data.sharedPrefs.UserSharedPreferences
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.data.TrackAdapter

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(getNewNetworkClient())
    }

    private fun getNewNetworkClient(): NetworkClient {
        return NetworkClientImpl()
    }

    fun provideTracksInteractorImpl(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideAdapter(): TrackAdapter {
        return TrackAdapter()
    }

    fun provideSharedPrefs(context: Context): UserSharedPreferences {
        return UserSharedPreferences(context)
    }

    fun getPrefs(key: String, context: Context): SharedPreferences {
        val sharedPrefs = context.getSharedPreferences(key, MODE_PRIVATE)
        return sharedPrefs
    }
}