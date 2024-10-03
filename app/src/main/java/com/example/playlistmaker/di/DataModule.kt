package com.example.playlistmaker.di

import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.playlistmaker.app.IS_NIGHT_SP_KEY
import com.example.playlistmaker.main.data.MainRepository
import com.example.playlistmaker.main.domain.MainRepositoryInterface
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerRepositoryInterface
import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.data.api.ITunesApi
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<NetworkClient> { NetworkClientImpl(get()) }
    single<SearchRepositoryInterface> { SearchRepository(get(), get()) }
    single<SettingsRepositoryInterface> { SettingsRepository(get(), get()) }
    single<NetworkClient> { NetworkClientImpl(get()) }
    single<MainRepositoryInterface> { MainRepository(get()) }
    single<PlayerRepositoryInterface> { PlayerRepository(get()) }

    single<TrackAdapter> { TrackAdapter() }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(IS_NIGHT_SP_KEY, MODE_PRIVATE)
    }
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory<MediaPlayer> { MediaPlayer() }

}