package com.example.playlistmaker.settings.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R

class OpenLinkUseCase(
    private val context: Context
) {
    fun execute() {
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