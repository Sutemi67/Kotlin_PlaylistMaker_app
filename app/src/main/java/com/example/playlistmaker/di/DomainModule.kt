package com.example.playlistmaker.di

import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.OpenLinkUseCase
import org.koin.dsl.module

val domainModule = module {
    single<TracksInteractor> { TracksInteractorImpl(get()) }
    factory<OpenLinkUseCase> { OpenLinkUseCase(get()) }
}
