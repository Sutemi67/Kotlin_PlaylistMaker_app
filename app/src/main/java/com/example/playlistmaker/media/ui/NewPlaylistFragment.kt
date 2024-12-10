package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment(
) : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private val vm by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistImagePlace.setImageURI(uri)
//                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.playlistImagePlace.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.createButton.setOnClickListener {
            vm.addPlaylist(
                name = binding.playlistName.text.toString(),
                description = binding.playlistDescription.text.toString()
            ) { result ->
                if (result) {
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "Playlist already exists", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}