package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.playlistmaker.databinding.FragmentTabFavouritesBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.OpenPlayerActivity
import com.example.playlistmaker.search.ui.TrackAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private val vm by viewModel<FavouritesViewModel>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: FragmentTabFavouritesBinding
    private var count = "Default count"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.getTracksCount()
        binding = FragmentTabFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackAdapter = TrackAdapter().apply {
            openPlayerActivity = object : OpenPlayerActivity {
                override fun openPlayerActivity(track: Track) {
                    val intent = Intent(requireContext(), PlayerActivity::class.java)
                    intent.putExtra(TRACK_ID, track.trackId)
                    intent.putExtra(TRACK_NAME, track.trackName)
                    intent.putExtra(ARTIST, track.artistName)
                    intent.putExtra(ARTWORK_URL, track.artworkUrl100)
                    intent.putExtra(COLLECTION_NAME, track.collectionName)
                    intent.putExtra(COUNTRY, track.country)
                    intent.putExtra(GENRE, track.primaryGenreName)
                    intent.putExtra(RELEASE_DATE, track.releaseDate)
                    intent.putExtra(TRACK_TIME_IN_MILLIS, track.trackTime)
                    intent.putExtra(PREVIEW_URL, track.previewUrl)
                    intent.putExtra(IS_FAVOURITE, track.isFavourite)
                    intent.putExtra(LATEST_TIME_ADDED, track.latestTimeAdded)
                    startActivity(intent)
                }
            }
        }
        binding.favouritesRecycler.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        vm.count.observe(viewLifecycleOwner) { count = it.toString() }
        setFavourites()
        Log.d("DATABASE", "Количество треков в базе: $count")
    }

    override fun onResume() {
        super.onResume()
        setFavourites()
    }

    private fun setFavourites() {
        vm.refreshFavourites()
        lifecycleScope.launch {
            vm.favouriteTracks.collect {
                trackAdapter.setData(it)

                if (it.isEmpty()) {
                    binding.favouritesRecycler.isVisible = false
                    binding.noFavouritesLabel.isVisible = true
                } else {
                    binding.favouritesRecycler.isVisible = true
                    binding.noFavouritesLabel.isVisible = false
                }
            }
        }
    }
}