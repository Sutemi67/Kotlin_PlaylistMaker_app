package com.example.playlistmaker.search.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchActivitySearchAction(
        expression: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            val fromRep = repository.refillTrackList(expression)
            consumer.consume(
                findTracks = fromRep.trackList,
                response = fromRep.responseCode
            )
        }
    }

    override fun settingsActivityShareAction(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_android_course))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }

    override fun settingsActivityOpenLinkAction(context: Context) {
        executor.execute {
            val supportMessage = Intent(Intent.ACTION_SENDTO)
            supportMessage.data = Uri.parse("mailto:")
            supportMessage.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(context.getString(R.string.email_support_message_sender))
            )
            supportMessage.putExtra(
                Intent.EXTRA_SUBJECT,
                context.getString(R.string.support_message_subject)
            )
            supportMessage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            supportMessage.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message))
            startActivity(context, supportMessage, null)
        }
    }

    override fun settingsActivityAgreementAction(context: Context) {
        executor.execute {
            val url = Uri.parse(context.getString(R.string.link_public_offer))
            val intent = Intent(Intent.ACTION_VIEW, url).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        }
    }
}
