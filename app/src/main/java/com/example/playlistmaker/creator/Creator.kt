package com.example.playlistmaker.creator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.TracksRepository
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.data.sharedPrefs.UserSharedPreferences
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TracksRepositoryInterface
import com.example.playlistmaker.search.data.TrackAdapter

object Creator {
    private fun getTracksRepository(): TracksRepositoryInterface {
        return TracksRepository(getNewNetworkClient())
    }

    private fun getNewNetworkClient(): NetworkClient {
        return NetworkClientImpl()
    }

    fun provideTracksInteractorImpl(): SearchInteractorInterface {
        return SearchInteractor(getTracksRepository())
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