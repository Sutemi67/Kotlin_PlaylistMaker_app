package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.app.HISTORY_KEY
import com.example.playlistmaker.app.database.data.TracksConverter
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.domain.SearchRepositoryInterface
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SearchRepository(
    private val networkClient: NetworkClient,
    private val context: Context,
    private val converter: TracksConverter,
    private val database: DatabaseRepositoryInterface
) : SearchRepositoryInterface {
    private var historyList: MutableList<Track> = mutableListOf()

    override suspend fun searchAction(expression: String): Flow<TrackListAndResponse> = flow {
        val response = networkClient.doRequestApi(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            with(response as TracksResponse) {
                val trackList = converter.mapToTracks(results)
                emitAll(
                    combine(
                        flowOf(trackList),
                        database.getFavouritesList()
                    ) { tracks, favouriteTracks ->
                        val favouriteIds = favouriteTracks.map { it.trackId }.toSet()
                        val updatedTrackList = tracks.map { track ->
                            track.copy(isFavourite = track.trackId in favouriteIds)
                        }
                        TrackListAndResponse(updatedTrackList, response.resultCode)
                    }
                )
            }
        } else {
            emit(TrackListAndResponse(emptyList(), response.resultCode))
        }
    }

    override suspend fun updateTrackFavouriteStatus(track: Track, isFavourite: Boolean) {
        if (isFavourite) {
            database.addTrackToFavourites(track)
        } else {
            database.deleteTrackFromFavourites(track)
        }
        Log.d("DATABASE", "Track ${track.trackId} favourite status updated: $isFavourite")
    }

    override fun getPrefs(): SharedPreferences {
        val sharedPrefs = context.getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        return sharedPrefs
    }



    override fun addTrackInHistory(track: Track) {
        historyList = getHistory().toMutableList()
        val existingTrackIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            historyList.removeAt(existingTrackIndex)
            historyList.add(0, track)
            Log.e(
                "DATABASE",
                "track updated and moved to the top, favourite = ${track.isFavourite}"
            )
        } else {
            if (historyList.size < 10) {
                historyList.add(0, track)
                Log.e("DATABASE", "track added")
            } else {
                historyList.removeAt(9)
                historyList.add(0, track)
                Log.e("DATABASE", "track added on 0 place, list is full")
            }
        }
        saveHistory()
    }

    override fun getHistory(): List<Track> {
        val itemType = object : TypeToken<List<Track>>() {}.type
        val spH = getPrefs()
        val json = spH.getString(HISTORY_KEY, null) ?: return mutableListOf()
        return Gson().fromJson(json, itemType)
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
