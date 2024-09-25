package com.example.playlistmaker.search.data.sharedPrefs

import android.content.Context

class UserSharedPreferences(private val context: Context) {
//    private var historyList: MutableList<Track> = mutableListOf()

//    private fun getPrefs(key: String, context: Context): SharedPreferences {
//        val sharedPrefs = context.getSharedPreferences(key, MODE_PRIVATE)
//        return sharedPrefs
//    }

//    fun getHistory(): List<Track> {
//        val itemType = object : TypeToken<List<Track>>() {}.type
//        val spH = getPrefs(key = HISTORY_KEY, context)
//        val json = spH.getString(HISTORY_KEY, null) ?: return mutableListOf()
//        return Gson().fromJson(json, itemType)
//    }

//    fun addTrackInHistory(track: Track) {
//        historyList = getHistory().toMutableList()
//        if (historyList.contains(track)) {
//            historyList.remove(track)
//            historyList.add(0, track)
//            Log.e("saving", "track replaced")
//        } else {
//            if (historyList.size < 10) {
//                historyList.add(0, track)
//                Log.e("saving", "track added")
//            } else {
//                historyList.removeAt(9)
//                historyList.add(0, track)
//                Log.e("saving", "track add on 0 place, list is full")
//            }
//        }
//        saveHistory()
//    }

//     private fun saveHistory() {
//        val prefs = getPrefs(key = HISTORY_KEY, context)
//        val json = Gson().toJson(historyList.toTypedArray())
//        prefs.edit().putString(HISTORY_KEY, json).apply()
//    }

//    fun clearHistory() {
//        historyList.clear()
//        saveHistory()
//    }

//    fun getIsNight(prefs: SharedPreferences) = prefs.getBoolean(IS_NIGHT_SP_KEY, false)
}