package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

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

    override fun shareClick(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_android_course))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }
}
