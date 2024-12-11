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
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
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

        binding.playlistsRecycler.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.playlistsRecycler.adapter = adapter

        vm.listState.observe(viewLifecycleOwner) {
            uiState(it)
        }
        binding.newPlaylistButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_fragmentSingleMedia_to_newPlaylistFragment)
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