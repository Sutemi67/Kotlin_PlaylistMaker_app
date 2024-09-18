package com.example.playlistmaker.domain

import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun doRequest(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            val fromRep = repository.refillTrackList(expression)
            consumer.consume(
                findTracks = fromRep.trackList,
                response = fromRep.responseCode
            )
        }
    }
}
