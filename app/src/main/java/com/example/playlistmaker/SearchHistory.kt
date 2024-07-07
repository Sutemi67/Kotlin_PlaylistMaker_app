package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.activities.HISTORY_KEY
import com.example.playlistmaker.recyclerView.Track
import com.google.gson.Gson

class SearchHistory {

    fun saveList(sharedPreferences: SharedPreferences, historyList: Array<Track>) {
        val jsonHistory = Gson().toJson(historyList)
        sharedPreferences.edit().putString(HISTORY_KEY, jsonHistory).apply()
    }

    fun getList(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray<Track>()
        return Gson().fromJson(json, Array<Track>::class.java)
    }
}