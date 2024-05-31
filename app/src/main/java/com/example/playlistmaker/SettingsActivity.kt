package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
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
        backButton.setOnClickListener {
            finish()
        }
        val agreementButton = findViewById<TextView>(R.id.button_agreement)
        agreementButton.setOnClickListener {
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
        val supportButton = findViewById<TextView>(R.id.button_support)
        supportButton.setOnClickListener {
            val message = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val supportMessage = Intent(Intent.ACTION_SENDTO)
            supportMessage.data = Uri.parse("mailto:")
            supportMessage.putExtra(Intent.EXTRA_EMAIL, arrayOf("sutemi67@gmail.com"))
            supportMessage.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportMessage)

        }
        val shareButton = findViewById<TextView>(R.id.button_share)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Message to share")
            startActivity(intent)
        }
    }
}