package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.activities.ARRAY
import com.example.playlistmaker.activities.HISTORY_KEY
import com.example.playlistmaker.recyclerView.Track
import com.google.gson.Gson

class SearchHistory {
    val array = Array<Track?>(10) { null }

    fun saveList(sharedPreferences: SharedPreferences, historyList: Array<Track?>) {
        val jsonHistory = Gson().toJson(historyList)
        sharedPreferences.edit().putString(ARRAY, jsonHistory).apply()
    }

    fun getList(sharedPreferences: SharedPreferences): Array<Track?>? {
        val json = sharedPreferences.getString(ARRAY, null) ?: return Array(10) { null }
        return Gson().fromJson(json, Array<Track?>::class.java)
    }
}