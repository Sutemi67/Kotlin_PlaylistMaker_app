package com.example.playlistmaker.savings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.activities.HISTORY_KEY
import com.example.playlistmaker.activities.SettingsActivity.Companion.IS_NIGHT
import com.example.playlistmaker.recyclerView.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory {

//    private val pref = context.getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)

    fun addHistory(spH: SharedPreferences ,history: ArrayList<Track>) {
        val json = Gson().toJson(history.toTypedArray())
        spH.edit().putString(HISTORY_KEY, json).apply()
    }

    fun getHistory(spH: SharedPreferences): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val json = spH.getString(HISTORY_KEY, null)
            ?: return ArrayList()
        return Gson().fromJson(json, itemType)
    }

    fun getIsNight(spNT: SharedPreferences) = spNT.getInt(IS_NIGHT, 1)
}