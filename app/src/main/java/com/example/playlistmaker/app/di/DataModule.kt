package com.example.playlistmaker.app.di

import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.room.RoomDatabase
import com.example.playlistmaker.app.IS_NIGHT_SP_KEY
import com.example.playlistmaker.app.database.data.DatabaseRepository
import com.example.playlistmaker.app.database.data.TracksDb
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.main.data.MainRepository
import com.example.playlistmaker.main.domain.MainRepositoryInterface
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerRepositoryInterface
import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.data.api.ITunesApi
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.NetworkClientImpl
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<NetworkClient> { NetworkClientImpl(get()) }
    single<SearchRepositoryInterface> { SearchRepository(get(), get(), get()) }
    single<SettingsRepositoryInterface> { SettingsRepository(get(), get()) }
    single<MainRepositoryInterface> { MainRepository(get()) }
    single<PlayerRepositoryInterface> { PlayerRepository(get()) }
    single<DatabaseRepositoryInterface> { DatabaseRepository(get()) }

    single<RoomDatabase> {
        TracksDb.getInstance(get())
    }

    single<OkHttpClient> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(IS_NIGHT_SP_KEY, MODE_PRIVATE)
    }

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory<MediaPlayer> { MediaPlayer() }

}