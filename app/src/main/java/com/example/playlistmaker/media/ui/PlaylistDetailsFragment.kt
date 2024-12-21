package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ARG_PLAYLIST
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.media.ui.observers.TrackLongClickListener
import com.example.playlistmaker.media.ui.stateInterfaces.TrackListState
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.OpenPlayerActivity
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlaylistDetailsFragment : Fragment() {

    companion object {
        fun createArgs(playlist: String): Bundle = bundleOf(
            ARG_PLAYLIST to playlist
        )
    }

    private lateinit var bottomSheetMenu: BottomSheetBehavior<ConstraintLayout>

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
        setOnclickListenersAndObservers()
        lifecycleScope.launch {
            vm.getPlaylistTracks(playlist)
        }
        val bottomSheetTracks = BottomSheetBehavior.from(binding.detailsBottomTracks)
        bottomSheetMenu = BottomSheetBehavior.from(binding.detailsBottomMenu)

        binding.detailsMore.post {
            val bottomPosition = binding.detailsMore.bottom
            val screenHeight = resources.displayMetrics.heightPixels
            val margin = ((screenHeight - bottomPosition) * 0.3).toInt()
            bottomSheetTracks.peekHeight = screenHeight - bottomPosition - margin
            Log.d(
                "log",
                "высота менюшки - ${bottomSheetTracks.peekHeight}\nвысота экрана ${screenHeight}\nвысота низа кнопки $bottomPosition\nотступ: $margin"
            )
        }
        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetTracks.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setBindingData() {
        binding.playlistTracksRecycler.adapter = adapter
        binding.detailsPlaylistName.text = playlist.name
        binding.detailsDescription.text = playlist.description
        val uri = playlist.coverUrl
        if (!uri.equals("null", ignoreCase = true)) {
            binding.detailsImageCover.setImageURI(uri?.toUri())
        }
    }

    private fun setOnclickListenersAndObservers() {
        binding.detailsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.detailsShare.setOnClickListener {
            if (playlist.tracks.isEmpty()) {
                vm.showDialog(requireContext())
            } else {
                vm.onShareClick(requireContext(), playlist)
            }
        }
        binding.detailsMore.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        vm.listState.observe(viewLifecycleOwner) {
            when (it) {
                is TrackListState.Empty -> {
                    Log.i("log", "список треков пуст")
                    adapter.setData(it.tracklist)
                    binding.detailsTracksCount.text = "Треков нет"
                    binding.detailsSummaryDuration.text = "0 минут"
                }

                is TrackListState.Filled -> {
                    Log.i("log", "${it.tracklist}")
                    adapter.setData(it.tracklist)
                    binding.detailsTracksCount.text = when (it.tracklist.size % 10) {
                        1 -> "${it.tracklist.size} трек"
                        in 2..4 -> "${it.tracklist.size} трека"
                        else -> "${it.tracklist.size} треков"
                    }
                    val totalDuration = it.tracklist.sumOf { it.trackTime }
                    binding.detailsSummaryDuration.text =
                        SimpleDateFormat(
                            "mm минут${
                                when ((totalDuration / 1000 / 60) % 10) {
                                    1 -> "а"
                                    in 2..4 -> "ы"
                                    else -> ""
                                }
                            }",
                            Locale.getDefault()
                        ).format(totalDuration)
                }
            }
        }
        adapter.longClickAction = object : TrackLongClickListener {
            override fun onTrackLongClick(track: Track) {
                val confirmDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Хотите удалить трек?")
                    .setNegativeButton("Нет") { dialog, which ->
                        //
                    }.setPositiveButton("Да") { dialog, which ->
                        lifecycleScope.launch {
                            vm.removeTrackFromPlaylist(track, playlist)
                            vm.getPlaylistTracks(playlist)
                        }
                    }
                confirmDialog.show()
            }
        }
        adapter.openPlayerActivity = object : OpenPlayerActivity {
            override fun openPlayerActivity(track: Track) {
                val json = Gson().toJson(track)
                findNavController().navigate(
                    R.id.action_playlistDetailsFragment_to_playerFragment,
                    PlayerFragment.createArgs(json)
                )
            }
        }
    }

}
