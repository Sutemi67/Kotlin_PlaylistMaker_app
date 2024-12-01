package com.example.playlistmaker.app.di

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.app.database.domain.DatabaseInteractor
import com.example.playlistmaker.app.database.domain.DatabaseInteractorInterface
import com.example.playlistmaker.main.domain.MainInteractor
import com.example.playlistmaker.main.domain.MainInteractorInterface
import com.example.playlistmaker.media.domain.MediaInteractor
import com.example.playlistmaker.media.domain.MediaInteractorInterface
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorInterface
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorInterface
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface
import org.koin.dsl.module

val domainModule = module {
    single<SearchInteractorInterface> { SearchInteractor(get()) }
    single<SettingsInteractorInterface> { SettingsInteractor(get()) }
    single<MainInteractorInterface> { MainInteractor(get()) }
    single<PlayerInteractorInterface> { PlayerInteractor(get()) }
    single<DatabaseInteractorInterface> { DatabaseInteractor(get()) }
    single<Handler> { Handler(Looper.getMainLooper()) }
    single<MediaInteractorInterface> { MediaInteractor(get()) }
}
