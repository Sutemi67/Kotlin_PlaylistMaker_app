package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.app.di.appModule
import com.example.playlistmaker.app.di.dataModule
import com.example.playlistmaker.app.di.domainModule
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
}