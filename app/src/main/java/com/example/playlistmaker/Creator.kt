package com.example.playlistmaker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.api.NetworkClient
import com.example.playlistmaker.data.api.NetworkClientImpl
import com.example.playlistmaker.data.sharedPrefs.UserSharedPreferences
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.TracksInteractorImpl
import com.example.playlistmaker.domain.TracksRepository

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

    fun provideSharedPrefs(): UserSharedPreferences {
        return UserSharedPreferences()
    }

    fun getPrefs(key: String, context: Context): SharedPreferences {
        val sharedPrefs = context.getSharedPreferences(key, MODE_PRIVATE)
        return sharedPrefs
    }
}