package com.example.playlistmaker.settings.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ComposeSettings() {
    Scaffold(
        topBar = { Text("Настройки") }
    ) { paddingValues ->
        Text("Привет, мир!", modifier = Modifier.padding(paddingValues))
    }
}

@Preview
@Composable
fun Hd() {
    ComposeSettings()
}