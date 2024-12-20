package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.app.ARG_PLAYLIST
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.media.ui.stateInterfaces.TrackListState
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlaylistDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val vm by viewModel<PlaylistDetailsViewModel>()
    private val adapter = TrackAdapter()

    private lateinit var playlist: Playlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val token = object : TypeToken<Playlist>() {}.type
            playlist = Gson().fromJson(it.getString(ARG_PLAYLIST), token)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBindingData()
        setOnclickListeners()

        lifecycleScope.launch {
            vm.getPlaylistTracks(playlist)
        }
    }

    private fun setBindingData() {
        binding.playlistTracksRecycler.adapter = adapter
        binding.detailsPlaylistName.text = playlist.name
        binding.detailsDescription.text = playlist.description
        binding.detailsTracksCount.text = when (playlist.count % 10) {
            0 -> "Нет треков"
            1 -> "${playlist.count} трек"
            in 2..4 -> "${playlist.count} трека"
            else -> "${playlist.count} треков"
        }
        val uri = playlist.coverUrl
        if (!uri.equals("null", ignoreCase = true)) {
            binding.detailsImageCover.setImageURI(uri?.toUri())
        }
        val totalDuration = playlist.tracks.sumOf { it.trackTime }
        binding.detailsSummaryDuration.text =
            SimpleDateFormat(
                "mm минут${if (totalDuration % 10 in 2..4 && (totalDuration < 10 || totalDuration > 20)) "ы" else ""}",
                Locale.getDefault()
            ).format(totalDuration)
        Log.i("log", "$totalDuration")
    }

    private fun setOnclickListeners() {
        binding.detailsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.detailsShare.setOnClickListener {

        }
        binding.detailsMore.setOnClickListener {

        }
        vm.listState.observe(viewLifecycleOwner) {
            when (it) {
                is TrackListState.Empty -> {
                    Log.i("log", "список треков пуст")
                }

                is TrackListState.Filled -> {
                    Log.i("log", "${it.tracklist}")
                    adapter.setData(it.tracklist)
                }
            }

        }
    }

    companion object {
        fun createArgs(playlist: String): Bundle = bundleOf(
            ARG_PLAYLIST to playlist
        )

    }
}
