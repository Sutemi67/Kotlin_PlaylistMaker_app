package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.common.HISTORY_KEY
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.settings.SettingsActivity.Companion.IS_NIGHT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Savings {

    var historyList = ArrayList<Track>()

    fun getHistory(spH: SharedPreferences): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val json = spH.getString(HISTORY_KEY, null)
            ?: return ArrayList()
        return Gson().fromJson(json, itemType)
    }

    fun getIsNight(spNT: SharedPreferences) = spNT.getInt(IS_NIGHT, 1)

}