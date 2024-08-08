package com.example.playlistmaker.activities

import android.media.MediaPlayer
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
import com.example.playlistmaker.ARTIST
import com.example.playlistmaker.ARTWORK_URL
import com.example.playlistmaker.COLLECTION_NAME
import com.example.playlistmaker.COUNTRY
import com.example.playlistmaker.GENRE
import com.example.playlistmaker.PREVIEW_URL
import com.example.playlistmaker.R
import com.example.playlistmaker.RELEASE_DATE
import com.example.playlistmaker.TRACK_NAME
import com.example.playlistmaker.TRACK_TIME_IN_MILLIS
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT

    private val mediaPlayer = MediaPlayer()
    private lateinit var playButton: ImageView
    private lateinit var previewUrl: String

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

        playButton = findViewById(R.id.player_play_button)
        val artistName: TextView = findViewById(R.id.ArtistName)
        val trackName: TextView = findViewById(R.id.TrackName)
        val time: TextView = findViewById(R.id.time)
        val duration: TextView = findViewById(R.id.player_duration)
        val collectionName: TextView = findViewById(R.id.player_album)
        val cover: ImageView = findViewById(R.id.player_cover)
        val country: TextView = findViewById(R.id.player_country)
        val primaryGenreName: TextView = findViewById(R.id.player_primaryGenre)
        val releaseYear: TextView = findViewById(R.id.player_releaseDate)

        artistName.text = intent.getStringExtra(ARTIST)
        trackName.text = intent.getStringExtra(TRACK_NAME)
        collectionName.text = intent.getStringExtra(COLLECTION_NAME)
        country.text = intent.getStringExtra(COUNTRY)
        primaryGenreName.text = intent.getStringExtra(GENRE)
        previewUrl = intent.getStringExtra(PREVIEW_URL).toString()

        releaseYear.text = intent.getStringExtra(RELEASE_DATE)?.substring(0, 4) ?: "-"

        val getDuration = intent.getIntExtra(TRACK_TIME_IN_MILLIS, 0)
        time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)

        fun coverResolutionAmplifier(): String? {
            return intent.getStringExtra(ARTWORK_URL)?.replaceAfterLast('/', "512x512bb.jpg")
        }


        Glide.with(this@PlayerActivity)
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.img_placeholder)
            .into(cover)

        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.audioplayer_button_pause_light)
        playerState = STATE_PLAYING

    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.audioplayer_button_play_light)
        playerState = STATE_PAUSED
    }
}
