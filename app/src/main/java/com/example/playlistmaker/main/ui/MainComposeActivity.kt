package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.main.navigation.PlaylistMakerApp
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainComposeActivity() : ComponentActivity() {

    val viewModel by viewModel<SingleActivityViewModel>()
    var darkTheme = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.result.collect { it ->
                darkTheme = it
            }
        }

        enableEdgeToEdge()
        setContent {

            PlaylistMakerTheme(
                darkTheme = darkTheme
            ) {
                PlaylistMakerApp()
            }
        }
    }

}


