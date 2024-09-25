package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.TracksRepository
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.domain.TracksRepositoryInterface
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface
import org.koin.dsl.module

val dataModule = module {
    single<NetworkClient> { NetworkClientImpl() }
    single<TracksRepositoryInterface> { TracksRepository(get()) }
    single<SettingsRepositoryInterface> { SettingsRepository(get()) }
}