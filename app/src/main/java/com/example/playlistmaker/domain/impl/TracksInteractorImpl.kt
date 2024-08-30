package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractorInterface
import com.example.playlistmaker.domain.TracksRepositoryInterface
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepositoryInterface) : TracksInteractorInterface {

    private val executor = Executors.newCachedThreadPool()

    override fun doRequest(expression: String, consumer: TracksInteractorInterface.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}