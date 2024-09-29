package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.data.TrackAdapter
import com.example.playlistmaker.search.data.api.ITunesApi
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<NetworkClient> { NetworkClientImpl(get()) }
    single<SearchRepositoryInterface> { SearchRepository(get(), get()) }
    single<SettingsRepositoryInterface> { SettingsRepository(get()) }
    single<NetworkClient> { NetworkClientImpl(get()) }
    single<TrackAdapter> { TrackAdapter() }
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

}