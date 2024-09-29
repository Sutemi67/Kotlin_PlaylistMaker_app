package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ARTIST
import com.example.playlistmaker.app.ARTWORK_URL
import com.example.playlistmaker.app.COLLECTION_NAME
import com.example.playlistmaker.app.COUNTRY
import com.example.playlistmaker.app.GENRE
import com.example.playlistmaker.app.PREVIEW_URL
import com.example.playlistmaker.app.RELEASE_DATE
import com.example.playlistmaker.app.TRACK_NAME
import com.example.playlistmaker.app.TRACK_TIME_IN_MILLIS
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    private lateinit var playButton: ImageView
    private lateinit var previewUrl: String
    private lateinit var currentTime: TextView
    private lateinit var binding: ActivityPlayerBinding
    private var timePlaying = 0L
    private var playerHandler: Handler? = null
    private val mediaPlayer = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.playerToolbar.setNavigationOnClickListener { finish() }

        playerHandler = Handler(Looper.getMainLooper())
        playButton = binding.playerPlayButton
        currentTime = binding.currentTime

        binding.ArtistName.text = intent.getStringExtra(ARTIST)
        binding.TrackName.text = intent.getStringExtra(TRACK_NAME)
        binding.collectionName.text = intent.getStringExtra(COLLECTION_NAME)
        binding.playerCountry.text = intent.getStringExtra(COUNTRY)
        binding.playerPrimaryGenre.text = intent.getStringExtra(GENRE)
        binding.releaseDate.text = intent.getStringExtra(RELEASE_DATE)?.substring(0, 4) ?: "-"
        previewUrl = intent.getStringExtra(PREVIEW_URL).toString()
        val getDuration = intent.getIntExtra(TRACK_TIME_IN_MILLIS, 0)

        currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timePlaying)
        binding.playerDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)

        fun coverResolutionAmplifier(): String? {
            return intent.getStringExtra(ARTWORK_URL)?.replaceAfterLast('/', "512x512bb.jpg")
        }

        Glide.with(this@PlayerActivity)
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.img_placeholder)
            .into(binding.playerCover)

        mediaPlayer.apply {
            try {
                setDataSource(previewUrl)
                prepareAsync()
                setOnCompletionListener {
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
            mediaPlayer.let {
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
        mediaPlayer.release()
        playerHandler?.removeCallbacks(timeCounter())
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.audioplayer_button_pause_light)
        playerHandler?.post(timeCounter())
    }

    private fun pausePlayer() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        playButton.setImageResource(R.drawable.audioplayer_button_play_light)
        playerHandler?.removeCallbacks(timeCounter())
    }

    private fun timeCounter(): Runnable {
        return Runnable {
            mediaPlayer.let {
                timePlaying = it.currentPosition.toLong()
                currentTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(timePlaying)
                playerHandler?.postDelayed(timeCounter(), 1000L)
            }
        }
    }
}

