package com.example.playlistmaker.player.ui

import android.os.Bundle
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
import com.example.playlistmaker.app.IS_FAVOURITE
import com.example.playlistmaker.app.LATEST_TIME_ADDED
import com.example.playlistmaker.app.PREVIEW_URL
import com.example.playlistmaker.app.RELEASE_DATE
import com.example.playlistmaker.app.TRACK_ID
import com.example.playlistmaker.app.TRACK_NAME
import com.example.playlistmaker.app.TRACK_TIME_IN_MILLIS
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageView
    private lateinit var previewUrl: String
    private lateinit var currentTime: TextView
    private lateinit var binding: ActivityPlayerBinding
    private val vm by viewModel<PlayerViewModel>()
    private lateinit var currentTrack: Track

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
        currentTrack = Track(
            trackId = intent.getIntExtra(TRACK_ID, 0),
            previewUrl = intent.getStringExtra(PREVIEW_URL) ?: "",
            trackName = intent.getStringExtra(TRACK_NAME) ?: "",
            artistName = intent.getStringExtra(ARTIST) ?: "",
            trackTime = intent.getIntExtra(TRACK_TIME_IN_MILLIS, 0),
            artworkUrl100 = intent.getStringExtra(ARTWORK_URL),
            country = intent.getStringExtra(COUNTRY) ?: "",
            collectionName = intent.getStringExtra(COLLECTION_NAME) ?: "",
            primaryGenreName = intent.getStringExtra(GENRE) ?: "",
            releaseDate = intent.getStringExtra(RELEASE_DATE)?.substring(0, 4) ?: "-",
            isFavourite = intent.getBooleanExtra(IS_FAVOURITE, false),
            latestTimeAdded = intent.getLongExtra(LATEST_TIME_ADDED, 0)
        )

        playButton = binding.playerPlayButton
        currentTime = binding.currentTime

        binding.ArtistName.text = currentTrack.artistName
        binding.TrackName.text = currentTrack.trackName
        binding.collectionName.text = currentTrack.collectionName
        binding.playerCountry.text = currentTrack.country
        binding.playerPrimaryGenre.text = currentTrack.primaryGenreName
        binding.releaseDate.text = currentTrack.releaseDate
        previewUrl = currentTrack.previewUrl.toString()
        val getDuration = currentTrack.trackTime

        currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
        binding.playerDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)

        fun coverResolutionAmplifier(): String? {
            return currentTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        }

        Glide.with(this@PlayerActivity)
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.img_placeholder)
            .into(binding.playerCover)

        vm.setPlayer(
            previewUrl = previewUrl,
            context = this
        )
        setClickListenersAndObservers()
    }

    override fun onPause() {
        super.onPause()
        vm.pausing()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.reset()
    }

    override fun onStop() {
        super.onStop()
        vm.reset()
    }

    private fun uiManaging(status: PlaybackStatus) {
        when (status) {
            PlaybackStatus.Playing -> {
                playButton.setImageResource(R.drawable.audioplayer_button_pause_light)
            }

            PlaybackStatus.Paused -> {
                playButton.setImageResource(R.drawable.audioplayer_button_play_light)
            }

            PlaybackStatus.Ready -> {
                playButton.setImageResource(R.drawable.audioplayer_button_play_light)
                currentTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
            }

            PlaybackStatus.Error -> {
                Toast.makeText(this, "Unsuccessful loading", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setClickListenersAndObservers() {

        vm.getPlaybackLiveData().observe(this) {
            uiManaging(it)
        }
        vm.getCounterText().observe(this) {
            currentTime.text = it
        }
        playButton.setOnClickListener {
            vm.playOrPauseAction()
        }
        binding.playerLike.setOnClickListener {
            vm.toggleFavourite(currentTrack)
        }
    }
}


