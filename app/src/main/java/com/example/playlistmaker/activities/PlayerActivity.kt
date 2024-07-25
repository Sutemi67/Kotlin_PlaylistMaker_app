package com.example.playlistmaker.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Toolbar>(R.id.player_toolbar).setNavigationOnClickListener { finish() }

        val artistName: TextView = findViewById(R.id.ArtistName)
        val trackName: TextView = findViewById(R.id.TrackName)
        val time: TextView = findViewById(R.id.time)
        val duration: TextView = findViewById(R.id.player_duration)
        val collectionName: TextView = findViewById(R.id.player_album)
        val cover: ImageView = findViewById(R.id.player_cover)
        val country: TextView = findViewById(R.id.player_country)
        val primaryGenreName: TextView = findViewById(R.id.player_primaryGenre)
        val releaseYear: TextView = findViewById(R.id.player_releaseDate)

        artistName.text = intent.getStringExtra("artist")
        trackName.text = intent.getStringExtra("trackName")
        collectionName.text = intent.getStringExtra("collectionName")
        country.text = intent.getStringExtra("country")
        primaryGenreName.text = intent.getStringExtra("primaryGenreName")

        releaseYear.text = intent.getStringExtra("releaseDate")?.substring(0, 4) ?: "-"

        val getDuration = intent.getIntExtra("trackTimeMillis", 0)
        time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)

        fun coverResolutionAmplifier(): String? {
            return intent.getStringExtra("artworkUrl100")?.replaceAfterLast('/', "512x512bb.jpg")
        }


        Glide.with(this@PlayerActivity)
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.img_placeholder)
            .into(cover)
    }

}