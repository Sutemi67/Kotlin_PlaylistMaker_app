package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface
import org.koin.dsl.module

val dataModule = module {
    single<NetworkClient> { NetworkClientImpl() }
    single<SearchRepositoryInterface> { SearchRepository(get(), get()) }
    single<SettingsRepositoryInterface> { SettingsRepository(get()) }
    single<NetworkClient> { NetworkClientImpl() }
}