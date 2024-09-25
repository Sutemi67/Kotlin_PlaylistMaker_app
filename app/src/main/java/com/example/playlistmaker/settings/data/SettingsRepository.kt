package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import com.example.playlistmaker.R
import com.example.playlistmaker.app.IS_CHECKED_SP_KEY
import com.example.playlistmaker.app.IS_NIGHT_SP_KEY
import com.example.playlistmaker.app.LIGHT_SP_VALUE
import com.example.playlistmaker.app.NIGHT_SP_VALUE
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface

class SettingsRepository(
    private val context: Context
) : SettingsRepositoryInterface {

    override fun openLinkAction() {
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

    override fun agreementAction() {
        val url = Uri.parse(context.getString(R.string.link_public_offer))
        val intent = Intent(Intent.ACTION_VIEW, url).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }

    override fun shareAction() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_android_course))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }

//    override fun getNightThemeSwitcher(): Boolean {
//        val nightModeSharedPrefs = context.getSharedPreferences(IS_NIGHT_SP_KEY, MODE_PRIVATE)
//        return nightModeSharedPrefs.getBoolean(IS_NIGHT_SP_KEY, false)
//    }

    override fun themeChangeAction(): Boolean {
        val nightModeSharedPrefs = context.getSharedPreferences(IS_NIGHT_SP_KEY, MODE_PRIVATE)
        val nightThemeSwitcher = nightModeSharedPrefs.getBoolean(IS_NIGHT_SP_KEY, false)
        Log.e("theme","changing from $nightThemeSwitcher")

        if (nightThemeSwitcher) {
            nightModeSharedPrefs.edit { putBoolean(IS_NIGHT_SP_KEY, false) }
            return true
        } else {
            nightModeSharedPrefs.edit { putBoolean(IS_NIGHT_SP_KEY, true) }
            return false
        }
    }
}