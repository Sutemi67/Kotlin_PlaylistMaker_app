package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var count = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTabFavouritesBinding.inflate(layoutInflater, container, false)
        trackAdapter = TrackAdapter().apply {
            openPlayerActivity = object : OpenPlayerActivity {
                override fun openPlayerActivity(track: Track) {
                    val intent = Intent(requireContext(), PlayerActivity::class.java)
                    intent.putExtra("TRACK_ID", track.trackId)
                    startActivity(intent)
                }
            }
        }
        binding.favouritesRecycler.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        vm.getTracksCount()
        observeFavouritesList()

        vm.count.observe(viewLifecycleOwner) { count = it }

        Log.d("DATABASE", "Количество треков в базе: $count")
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeFavouritesList()
    }

    private fun observeFavouritesList() {
        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.favouriteTracks.collect { tracks ->
                trackAdapter.setData(tracks)
            }
        }
    }

}