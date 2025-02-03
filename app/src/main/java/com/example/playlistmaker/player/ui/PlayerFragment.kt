package com.example.playlistmaker.player.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ARG_TRACK2
import com.example.playlistmaker.app.LostConnectionBroadcastReceiver
import com.example.playlistmaker.app.PlayerService
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {

    companion object {
        fun createArgs(track: String): Bundle = bundleOf(ARG_TRACK2 to track)
    }

    private lateinit var binding: FragmentPlayerBinding
    private val vm by viewModel<PlayerViewModel>()
    private lateinit var previewUrl: String
    private lateinit var currentTrack: Track
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetContainer: LinearLayout
    private val adapter = PlayerAdapter()
    private val br by lazy { LostConnectionBroadcastReceiver(requireView()) }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), "Can't bind service!", Toast.LENGTH_LONG).show()
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerServiceBinder
            vm.setAudioPlayerControl(binder.getService())
            Log.i("MusicService", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            vm.removeAudioPlayerControl()
            Log.e("MusicService", "service disconnected")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val token = object : TypeToken<Track>() {}.type
            currentTrack = Gson().fromJson(it.getString(ARG_TRACK2), token)
        }
        bindMusicService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.ArtistName.text = currentTrack.artistName
        binding.TrackName.text = currentTrack.trackName
        binding.collectionName.text = currentTrack.collectionName
        binding.playerCountry.text = currentTrack.country
        binding.playerPrimaryGenre.text = currentTrack.primaryGenreName
        binding.releaseDate.text = currentTrack.releaseDate
        previewUrl = currentTrack.previewUrl.toString()

        bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.bottomList.adapter = adapter

        Glide.with(requireActivity())
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(40))
            .placeholder(R.drawable.img_placeholder)
            .into(binding.playerCover)

        setClickListenersAndObservers()

        adapter.addTrackInPlaylist = object : AddingTrackInPlaylistInterface {
            override fun addTrackInPlaylist(playlist: Playlist) {
                vm.addInPlaylist(currentTrack, playlist)
                vm.addingStatus.observe(viewLifecycleOwner) {
                    if (it.state) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), PlayerService::class.java).apply {
            putExtra("song_url", currentTrack.previewUrl)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d("MusicService", "bind service")
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
        Log.e("MusicService", "unbind service")
    }

    private fun coverResolutionAmplifier(): String? {
        return currentTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun updateButtonAndProgress(state: PlayerState) {
        binding.playerPlayButton.setPlaybackIcon(state)
        binding.currentTime.text = state.progress
    }

    private fun setClickListenersAndObservers() {

        vm.setFavouriteState(currentTrack)

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

        binding.playerPlayButton.setOnPlaybackClickListener {
            vm.onPlayerButtonClicked()
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
        vm.observePlayerState().observe(viewLifecycleOwner) {
            updateButtonAndProgress(it)
            Log.i("MusicService", "статус - $it")
        }
    }

    //region Other fragment-lifecycle methods
    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(br)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            br, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    override fun onDestroy() {
        unbindMusicService()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }
//endregion
}