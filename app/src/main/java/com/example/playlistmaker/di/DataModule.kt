package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.domain.TracksRepository
import org.koin.dsl.module

val dataModule = module{
    single<NetworkClient> { NetworkClientImpl() }
    single<TracksRepository> { TracksRepositoryImpl(get()) }
}