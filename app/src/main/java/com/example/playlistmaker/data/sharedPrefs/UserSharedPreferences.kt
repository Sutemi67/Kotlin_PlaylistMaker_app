package com.example.playlistmaker.data.sharedPrefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.common.HISTORY_KEY
import com.example.playlistmaker.common.IS_NIGHT_SP_KEY
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserSharedPreferences(private val context: Context) {
    private var historyList: List<Track> = mutableListOf()

    private fun getPrefs(key: String, context: Context): SharedPreferences {
        val sharedPrefs = context.getSharedPreferences(key, MODE_PRIVATE)
        return sharedPrefs
    }

    fun getTracksHistory(): List<Track> {
        val itemType = object : TypeToken<List<Track>>() {}.type
        val spH = getPrefs(key = HISTORY_KEY, context)
        val json = spH.getString(HISTORY_KEY, null)
            ?: return mutableListOf()
        return Gson().fromJson(json, itemType)
    }

    fun addTrackInHistory(track: Track) {
        if (historyList.contains(track)) {
            historyList.toMutableList().remove(track)
            historyList.toMutableList().add(0, track)
        } else {
            if (historyList.size < 10) {
                historyList.toMutableList().add(0, track)
            }
            else{
                historyList.toMutableList().removeAt(9)
                historyList.toMutableList().add(0, track)
            }
        }
        saveHistory()
    }

    private fun saveHistory() {
        val prefs = getPrefs(key = HISTORY_KEY, context)
        val json = Gson().toJson(historyList.toTypedArray())
        prefs.edit().putString(HISTORY_KEY, json).apply()
    }

    fun clearHistory() {
        historyList = emptyList()
        saveHistory()
    }

    fun getIsNight(prefs: SharedPreferences) = prefs.getInt(IS_NIGHT_SP_KEY, 1)
}