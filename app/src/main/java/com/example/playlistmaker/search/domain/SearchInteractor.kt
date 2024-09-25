package com.example.playlistmaker.search.domain

import java.util.concurrent.Executors

class SearchInteractor(
    private val repository: TracksRepositoryInterface
) : SearchInteractorInterface {

    private val executor = Executors.newCachedThreadPool()

    override fun searchActivitySearchAction(
        expression: String,
        consumer: SearchInteractorInterface.TracksConsumer
    ) {
        executor.execute {
            val fromRep = repository.refillTrackList(expression)
            consumer.consume(
                findTracks = fromRep.trackList,
                response = fromRep.responseCode
            )
        }
    }
}
