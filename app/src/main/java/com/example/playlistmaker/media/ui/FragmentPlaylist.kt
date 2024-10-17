package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding

class FragmentPlaylist : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(PLAYLIST_BUNDLE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        private const val PLAYLIST_BUNDLE_KEY = "playlist_key"

        @JvmStatic
        fun newInstance() =
            FragmentPlaylist().apply {
                arguments = Bundle().apply {
                    putString(PLAYLIST_BUNDLE_KEY, param1)
                }
            }
    }
}