package com.example.playlistmaker.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.playlistmaker.settings.ui.ComposeSettings
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object SettingsScreenRoute

fun NavController.navigateToSettings(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = SettingsScreenRoute, navOptions)

fun NavGraphBuilder.settingsScreen(onBackClick: () -> Unit) {
    composable<SettingsScreenRoute> { ComposeSettings(koinViewModel(), onBackClick = onBackClick) }
}