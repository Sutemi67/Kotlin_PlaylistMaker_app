package com.example.playlistmaker.media.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment(
) : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val vm by viewModel<NewPlaylistViewModel>()
    private var coverUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createButton.isEnabled = false
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                getPermissions(uri)
                saveImageToPrivateStorage(uri)
                binding.playlistImagePlace.setImageURI(uri)
            }
        }
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                //
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }

        val playlistNameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                binding.createButton.isEnabled = !s.isNullOrEmpty()
                setConfirmDialogCallback()
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        val playlistDescriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                setConfirmDialogCallback()
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        binding.playlistName.addTextChangedListener(playlistNameTextWatcher)
        binding.playlistDescription.addTextChangedListener(playlistDescriptionTextWatcher)

        requireActivity().onBackPressedDispatcher.addCallback(this, dialogCallback)
        setOnClickListeners()

    }

    //TODO подумать над упрощением колбэка, может убрать совсем.
    val dialogCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.playlistName.text?.isEmpty() == false ||
                binding.playlistDescription.text?.isEmpty() == false ||
                coverUri != null
            ) {
                confirmDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun setConfirmDialogCallback() {
        if (binding.playlistName.text?.isEmpty() == false ||
            binding.playlistDescription.text?.isEmpty() == false ||
            coverUri != null
        ) {
            val dialogCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmDialog.show()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, dialogCallback)
        }
    }

    private fun showSnackBar(text: String) {
        val snack = Snackbar.make(requireView(), text, 3000)
        snack.view.setBackgroundResource(R.drawable.shape_toast)
        val textView =
            snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textSize = 16f
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.gravity = Gravity.CENTER
        snack.show()
    }

    private fun getPermissions(uri: Uri) {
        val takeFlags: Int =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        try {
            requireContext().contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: SecurityException) {
            Log.e(
                "DATABASE",
                "Persistable permission not supported for this URI: $uri, ${e.message}"
            )
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlists_covers"
        )
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, "${System.currentTimeMillis()}_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        coverUri = file.toUri()
    }

    private fun setOnClickListeners() {
        binding.playlistImagePlace.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.toolbar.setNavigationOnClickListener {
            if (binding.playlistName.text?.isEmpty() == false ||
                binding.playlistDescription.text?.isEmpty() == false ||
                coverUri != null
            ) {
                confirmDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.createButton.setOnClickListener {
            vm.addPlaylist(
                name = binding.playlistName.text.toString(),
                description = binding.playlistDescription.text.toString(),
                image = coverUri.toString()
            ) { result ->
                if (result) {
                    showSnackBar("Плейлист ${binding.playlistName.text} создан")
                    findNavController().navigateUp()
                } else {
                    showSnackBar("Такой плейлист уже существует")
                }
            }
        }
    }
}