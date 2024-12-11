package com.example.playlistmaker.media.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                // ничего не делаем
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }
        setOnClickListeners()
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
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlists_covers"
            )
        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "${System.currentTimeMillis()}_cover.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
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
                    findNavController().navigateUp()
                } else {
                    Toast
                        .makeText(requireContext(), "Playlist already exists", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}