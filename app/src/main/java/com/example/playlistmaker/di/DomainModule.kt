package com.example.playlistmaker.di

import com.example.playlistmaker.main.domain.MainInteractor
import com.example.playlistmaker.main.domain.MainInteractorInterface
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface
import org.koin.dsl.module

val domainModule = module {
    single<SearchInteractorInterface> { SearchInteractor(get()) }
    single<SettingsInteractorInterface> { SettingsInteractor(get()) }
    single<MainInteractorInterface> { MainInteractor(get()) }
}
