package com.example.playlistmaker.settings.domain

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R

class ShareUseCase(
    private val context: Context
) {
    fun execute() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_android_course))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }
}