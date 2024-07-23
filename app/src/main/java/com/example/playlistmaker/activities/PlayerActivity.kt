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

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

        val artistName: TextView = findViewById(R.id.ArtistName)
        val trackName: TextView = findViewById(R.id.TrackName)
        val duration: TextView = findViewById(R.id.time)
        val collectionName: TextView = findViewById(R.id.album)
        val cover: ImageView = findViewById(R.id.cover)
        val country: TextView = findViewById(R.id.country)
        val primaryGenreName: TextView = findViewById(R.id.primaryGenreName)
        val releaseYear: TextView = findViewById(R.id.releaseDate)

        artistName.text = intent.getStringExtra("artist")
        trackName.text = intent.getStringExtra("trackName")
        duration.text = intent.getStringExtra("trackTimeMillis")
        collectionName.text = intent.getStringExtra("collectionName")
        country.text = intent.getStringExtra("country")
        primaryGenreName.text = intent.getStringExtra("primaryGenreName")

        val releaseDate = intent.getStringExtra("releaseDate").toString()
        val upToNCharacters: String = releaseDate.substring(0, 3)
        releaseYear.text = upToNCharacters


        Glide.with(this@PlayerActivity)
            .load(intent.getStringExtra("artworkUrl100"))
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.img_placeholder)
            .into(cover)
    }

}