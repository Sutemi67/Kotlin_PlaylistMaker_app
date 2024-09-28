package com.example.playlistmaker.app

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(this@App)
//            modules(dataModule, domainModule, appModule)
//        }
    }
}