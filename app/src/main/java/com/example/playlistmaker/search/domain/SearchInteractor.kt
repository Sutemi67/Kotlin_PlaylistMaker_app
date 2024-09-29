package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class SearchInteractor(
    private val repository: SearchRepositoryInterface
) : SearchInteractorInterface {

    private val executor = Executors.newCachedThreadPool()

    override fun searchAction(
        expression: String,
        consumer: TracksConsumer
    ) {
        executor.execute {
            val fromRep = repository.searchAction(expression)
            consumer.consume(
                findTracks = fromRep.trackList,
                response = fromRep.responseCode
            )
        }
    }

    override fun getPrefs(): SharedPreferences {
        return repository.getPrefs()
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun addTrackInHistory(track: Track) {
        repository.addTrackInHistory(track)
    }

    override fun saveHistory() {
        repository.saveHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}
