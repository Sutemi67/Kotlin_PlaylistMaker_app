package com.example.playlistmaker.main.data

import android.content.SharedPreferences
import com.example.playlistmaker.app.IS_NIGHT_SP_KEY
import com.example.playlistmaker.main.domain.MainRepositoryInterface

class MainRepository(
    private val preferences: SharedPreferences
) : MainRepositoryInterface {
    override fun getTheme(): Boolean = preferences.getBoolean(IS_NIGHT_SP_KEY, false)
}