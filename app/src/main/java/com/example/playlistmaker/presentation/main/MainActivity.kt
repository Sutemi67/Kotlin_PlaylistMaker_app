package com.example.playlistmaker.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.common.IS_NIGHT_SP_KEY
import com.example.playlistmaker.presentation.media.MediaActivity
import com.example.playlistmaker.presentation.search.SearchActivity
import com.example.playlistmaker.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPreferences = Creator.provideSharedPrefs(applicationContext)

        val spNT = Creator.getPrefs(IS_NIGHT_SP_KEY, applicationContext)
        setDefaultNightMode(sharedPreferences.getIsNight(spNT))

        val buttonSearch = findViewById<Button>(R.id.button_search)
        buttonSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}