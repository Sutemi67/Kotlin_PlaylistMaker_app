package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.ui.observers.PlaylistClickListener
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val vm by viewModel<PlaylistsViewModel>()
    private val adapter = PlaylistsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = binding.playlistsRecycler
        val columnsCount = 2
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        recycler.layoutManager = GridLayoutManager(requireContext(), columnsCount)
        recycler.addItemDecoration(GridSpacingItemDecoration(columnsCount, spacing, true))

        binding.playlistsRecycler.adapter = adapter

        vm.listState.observe(viewLifecycleOwner) {
            uiState(it)
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_fragmentSingleMedia_to_newPlaylistFragment)
        }

        adapter.playlistClickListener = object : PlaylistClickListener {
            override fun onClick(playlist: Playlist) {
                val playlistJson = Gson().toJson(playlist)
                findNavController().navigate(
                    R.id.action_fragmentSingleMedia_to_playlistDetailsFragment,
                    PlaylistDetailsFragment.createArgs(playlistJson)
                )
            }
        }

        getPlaylists()
    }


    override fun onResume() {
        super.onResume()
        getPlaylists()
    }

    private fun getPlaylists() {
        lifecycleScope.launch {
            vm.getPlaylists()
        }
    }

    private fun uiState(state: PlaylistState) {
        when (state) {
            is PlaylistState.FullList -> {
                adapter.setData(state.playlist)
                binding.playlistsRecycler.isVisible = true
                binding.noPlaylists.isVisible = false
            }

            is PlaylistState.EmptyList -> {
                adapter.setData(emptyList())
                binding.playlistsRecycler.isVisible = false
                binding.noPlaylists.isVisible = true
            }
        }
    }
}