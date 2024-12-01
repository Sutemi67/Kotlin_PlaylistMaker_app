package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.app.di.appModule
import com.example.playlistmaker.app.di.dataModule
import com.example.playlistmaker.app.di.domainModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, appModule)
        }
    }

    private fun createDefaultOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    }
}