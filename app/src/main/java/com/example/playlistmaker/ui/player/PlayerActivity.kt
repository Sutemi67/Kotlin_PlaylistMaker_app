package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.common.ARTIST
import com.example.playlistmaker.common.ARTWORK_URL
import com.example.playlistmaker.common.COLLECTION_NAME
import com.example.playlistmaker.common.COUNTRY
import com.example.playlistmaker.common.GENRE
import com.example.playlistmaker.common.PREVIEW_URL
import com.example.playlistmaker.R
import com.example.playlistmaker.common.RELEASE_DATE
import com.example.playlistmaker.common.TRACK_NAME
import com.example.playlistmaker.common.TRACK_TIME_IN_MILLIS
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private var timePlaying = 0L
    private var mediaPlayer: MediaPlayer? = null
    private var playerHandler: Handler? = null

    private lateinit var playButton: ImageView
    private lateinit var previewUrl: String
    private lateinit var currentTime: TextView

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

        playerHandler = Handler(Looper.getMainLooper())
        playButton = findViewById(R.id.player_play_button)
        currentTime = findViewById(R.id.current_time)

        val artistName: TextView = findViewById(R.id.ArtistName)
        val trackName: TextView = findViewById(R.id.TrackName)
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
        currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timePlaying)
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

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(previewUrl)
                prepareAsync()
                setOnCompletionListener {
                    mediaPlayer?.seekTo(0)
                    playButton.setImageResource(R.drawable.audioplayer_button_play_light)
                    playerHandler?.removeCallbacks(timeCounter())
                    timePlaying = 0L
                    currentTime.text =
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(timePlaying)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    this@PlayerActivity,
                    R.string.player_error_loading_preview,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        playButton.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    pausePlayer()
                } else {
                    startPlayer()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        playerHandler?.removeCallbacks(timeCounter())
    }

    private fun startPlayer() {
        mediaPlayer?.start()
        playButton.setImageResource(R.drawable.audioplayer_button_pause_light)
        playerHandler?.post(timeCounter())
    }

    private fun pausePlayer() {
        mediaPlayer?.pause()
        playButton.setImageResource(R.drawable.audioplayer_button_play_light)
        playerHandler?.removeCallbacks(timeCounter())
    }

    private fun timeCounter(): Runnable {
        return Runnable {
            mediaPlayer?.let {
                timePlaying = it.currentPosition.toLong()
                currentTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(timePlaying)
                playerHandler?.postDelayed(timeCounter(), 1000L)
            }
        }
    }
}

