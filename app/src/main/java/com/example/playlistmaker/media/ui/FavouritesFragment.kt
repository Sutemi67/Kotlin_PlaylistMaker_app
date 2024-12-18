package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ARG_TRACK
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.OpenPlayerActivity
import com.example.playlistmaker.search.ui.TrackAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private val vm by viewModel<FavouritesViewModel>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackAdapter = TrackAdapter().apply {
            openPlayerActivity = object : OpenPlayerActivity {
                override fun openPlayerActivity(track: Track) {
                    val bundle = Bundle().apply {
                        putParcelable(ARG_TRACK, track)
                    }
                    findNavController().navigate(
                        R.id.action_fragmentSingleMedia_to_playerFragment,
                        bundle
                    )
                }
            }
        }

        binding.favouritesRecycler.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        setFavourites()
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