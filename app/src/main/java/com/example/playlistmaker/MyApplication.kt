package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.example.playlistmaker.activities.SettingsActivity.Companion.IS_NIGHT
import com.example.playlistmaker.recyclerView.TrackAdapter

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        val spNT = getSharedPreferences(IS_NIGHT, MODE_PRIVATE)
//        setDefaultNightMode(savings.getIsNight(spNT))
        val adapter = TrackAdapter()
    }
}