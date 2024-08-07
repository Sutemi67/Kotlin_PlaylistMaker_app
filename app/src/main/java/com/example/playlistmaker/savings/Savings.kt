package com.example.playlistmaker.savings

import android.content.SharedPreferences
import com.example.playlistmaker.HISTORY_KEY
import com.example.playlistmaker.activities.SettingsActivity.Companion.IS_NIGHT
import com.example.playlistmaker.recyclerView.Track
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