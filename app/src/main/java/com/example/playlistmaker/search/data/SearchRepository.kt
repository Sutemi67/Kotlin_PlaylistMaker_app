package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.app.HISTORY_KEY
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepository(
    private val networkClient: NetworkClient,
    private val context: Context
) : SearchRepositoryInterface {
    private var historyList: MutableList<Track> = mutableListOf()

    override suspend fun searchAction(expression: String): Flow<TrackListAndResponse> = flow {
        val response = networkClient.doRequestApi(TracksSearchRequest(expression))

        if (response.resultCode == 200) {
            with(response as TracksResponse) {
                val trackList = results.map {
                    Track(
                        it.trackId,
                        it.previewUrl,
                        it.trackName,
                        it.artistName,
                        it.trackTime,
                        it.artworkUrl100,
                        it.country,
                        it.collectionName,
                        it.primaryGenreName,
                        it.releaseDate
                    )
                }
                emit(TrackListAndResponse(trackList, response.resultCode))
            }
        } else {
            emit(TrackListAndResponse(emptyList(), response.resultCode))
        }
    }

    override fun getPrefs(): SharedPreferences {
        val sharedPrefs = context.getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        return sharedPrefs
    }

    override fun getHistory(): List<Track> {
        val itemType = object : TypeToken<List<Track>>() {}.type
        val spH = getPrefs()
        val json = spH.getString(HISTORY_KEY, null) ?: return mutableListOf()
        return Gson().fromJson(json, itemType)
    }

    override fun addTrackInHistory(track: Track) {
        historyList = getHistory().toMutableList()
        if (historyList.remove(track)) {
            historyList.add(0, track)
            Log.e("saving", "track replaced")
        } else {
            if (historyList.size < 10) {
                historyList.add(0, track)
                Log.e("saving", "track added")
            } else {
                historyList.removeAt(9)
                historyList.add(0, track)
                Log.e("saving", "track add on 0 place, list is full")
            }
        }
        saveHistory()
    }

    override fun saveHistory() {
        val prefs = getPrefs()
        val json = Gson().toJson(historyList.toTypedArray())
        prefs.edit().putString(HISTORY_KEY, json).apply()
    }

    override fun clearHistory() {
        historyList.clear()
        saveHistory()
    }
}
