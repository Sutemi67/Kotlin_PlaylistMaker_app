package com.example.playlistmaker.data.sharedPrefs

import android.content.SharedPreferences
import com.example.playlistmaker.common.HISTORY_KEY
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.settings.SettingsActivity.Companion.IS_NIGHT_SP_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserSharedPreferences {
    var historyList = ArrayList<Track>()

    fun getHistory(spH: SharedPreferences): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val json = spH.getString(HISTORY_KEY, null)
            ?: return ArrayList()
        return Gson().fromJson(json, itemType)
    }

    fun addHistory(
        preferencesForTrackHistory: SharedPreferences,
        history: ArrayList<Track>
    ) {
        val json = Gson().toJson(history.toTypedArray())
        preferencesForTrackHistory.edit().putString(HISTORY_KEY, json).apply()
    }

    fun getIsNight(prefs: SharedPreferences) = prefs.getInt(IS_NIGHT_SP_KEY, 1)
}