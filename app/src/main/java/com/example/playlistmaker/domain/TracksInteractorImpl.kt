package com.example.playlistmaker.domain

import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) :
    TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun doRequest(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.refillTrackList(expression))
        }
        t.start()
    }
}