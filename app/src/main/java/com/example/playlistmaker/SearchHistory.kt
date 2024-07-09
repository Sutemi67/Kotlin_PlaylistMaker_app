package com.example.playlistmaker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.activities.HISTORY_KEY
import com.example.playlistmaker.recyclerView.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(context: Context) {

    private val pref = context.getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)

    fun addHistory(history: ArrayList<Track>) {
        val json = Gson().toJson(history.toTypedArray())
        pref.edit().putString(HISTORY_KEY, json).apply()
    }

    fun getHistory(): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        val json = pref.getString(HISTORY_KEY, null)
            ?: return ArrayList()
        return Gson().fromJson(json, itemType)
    }
}