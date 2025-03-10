package com.example.playlistmaker.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.search.navigation.SearchScreenBaseRoute
import com.example.playlistmaker.search.navigation.searchScreen
import com.example.playlistmaker.settings.navigation.navigateToSettings
import com.example.playlistmaker.settings.navigation.settingsScreen

@Composable
fun PlaylistMakerApp(
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SearchScreenBaseRoute
    ) {
        searchScreen(toSettingsClick = navController::navigateToSettings)
        settingsScreen(onBackClick = navController::popBackStack)
    }
}