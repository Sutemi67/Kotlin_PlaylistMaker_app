package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ARG_TRACK
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.ui.PlaylistState
import com.example.playlistmaker.player.data.PlaybackStatus
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private val vm by viewModel<PlayerViewModel>()
    private lateinit var playButton: ImageView
    private lateinit var previewUrl: String
    private lateinit var currentTime: TextView
    private lateinit var currentTrack: Track
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetContainer: LinearLayout
    private val adapter = PlayerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playerToolbar.setNavigationOnClickListener {
//            findNavController().navigateUp()
            findNavController().popBackStack()
        }
        try {
            arguments?.getParcelable<Track>(ARG_TRACK)?.let { track ->
                currentTrack = track
            }
        } catch (e: Exception) {
            Log.e("DATABASE", e.message.toString())
        }

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

        bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.bottomList.adapter = adapter

        currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
        binding.playerDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(getDuration)

        Glide.with(requireActivity())
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.img_placeholder)
            .into(binding.playerCover)


        setClickListenersAndObservers()

        adapter.addTrackInPlaylist = object : AddingTrackInPlaylistInterface {
            override fun addTrackInPlaylist(playlist: Playlist) {
                vm.addInPlaylist(currentTrack, playlist)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
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


    private fun coverResolutionAmplifier(): String? {
        return currentTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
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
                Toast.makeText(requireContext(), "Unsuccessful loading", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setClickListenersAndObservers() {

        vm.setPlayer(
            previewUrl = previewUrl,
            context = requireContext(),
        )
        vm.setFavouriteState(currentTrack)

        vm.getPlaybackLiveData().observe(viewLifecycleOwner) {
            uiManaging(it)
        }
        vm.addingStatus.observe(viewLifecycleOwner) {
            if (it.state) Toast.makeText(
                requireContext(),
                "Добавлено в плейлист ${it.playlist.name}",
                Toast.LENGTH_SHORT
            ).show()
            else Toast.makeText(
                requireContext(),
                "Трек уже добавлен в плейлист ${it.playlist.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
        vm.getCounterText().observe(viewLifecycleOwner) {
            currentTime.text = it
        }
        vm.getLikeState().observe(viewLifecycleOwner) {
            if (it) {
                binding.playerLike.setImageResource(R.drawable.like_button_active)
            } else {
                binding.playerLike.setImageResource(R.drawable.like_button)
            }
        }
        vm.listState.observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.EmptyList -> {
//
                }

                is PlaylistState.FullList -> {
                    adapter.setData(it.playlist)
                    Log.e("DATABASE", "список загружен, плейлистов - ${it.playlist.size}")
                }
            }
        }

        playButton.setOnClickListener {
            vm.playOrPauseAction()
        }
        binding.playerLike.setOnClickListener {
            vm.toggleFavourite(currentTrack)
            currentTrack.isFavourite = !currentTrack.isFavourite
        }
        binding.playerAddPlaylistButton.setOnClickListener {
            lifecycleScope.launch {
                Log.i("DATABASE", "пошел запрос списка")
                vm.getPlaylists()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }
}


