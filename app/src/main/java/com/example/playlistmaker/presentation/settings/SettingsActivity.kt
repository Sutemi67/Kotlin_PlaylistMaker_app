package com.example.playlistmaker.presentation.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.common.IS_CHECKED_SP_KEY
import com.example.playlistmaker.common.IS_NIGHT_SP_KEY
import com.example.playlistmaker.common.LIGHT_SP_VALUE
import com.example.playlistmaker.common.NIGHT_SP_VALUE

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener { finish() }

        val agreementButton = findViewById<TextView>(R.id.button_agreement)
        agreementButton.setOnClickListener {
            val url = Uri.parse(this.getString(R.string.link_public_offer))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
        val supportButton = findViewById<TextView>(R.id.button_support)
        supportButton.setOnClickListener {
            val supportMessage = Intent(Intent.ACTION_SENDTO)
            supportMessage.data = Uri.parse("mailto:")
            supportMessage.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(this.getString(R.string.email_support_message_sender))
            )
            supportMessage.putExtra(
                Intent.EXTRA_SUBJECT,
                this.getString(R.string.support_message_subject)
            )
            supportMessage.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.support_message))
            startActivity(supportMessage)
        }

        val shareButton = findViewById<TextView>(R.id.button_share)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.link_to_android_course))
            startActivity(intent)
        }

        val checkSharedPrefs = getSharedPreferences(IS_CHECKED_SP_KEY, MODE_PRIVATE)
        val nightModeSharedPrefs = getSharedPreferences(IS_NIGHT_SP_KEY, MODE_PRIVATE)

        val nightThemeSwitcher = findViewById<Switch>(R.id.nightThemeSwitch)
        nightThemeSwitcher.isChecked = checkSharedPrefs.getBoolean(IS_CHECKED_SP_KEY, false)

        nightThemeSwitcher.setOnClickListener {
            if (nightThemeSwitcher.isChecked) {
                setDefaultNightMode(MODE_NIGHT_YES)
                checkSharedPrefs.edit {
                    putBoolean(
                        IS_CHECKED_SP_KEY,
                        nightThemeSwitcher.isChecked
                    )
                }
                nightModeSharedPrefs.edit { putInt(IS_NIGHT_SP_KEY, NIGHT_SP_VALUE) }
            } else {
                setDefaultNightMode(MODE_NIGHT_NO)
                checkSharedPrefs.edit {
                    putBoolean(
                        IS_CHECKED_SP_KEY,
                        nightThemeSwitcher.isChecked
                    )
                }
                nightModeSharedPrefs.edit { putInt(IS_NIGHT_SP_KEY, LIGHT_SP_VALUE) }
            }
        }
    }
}