package com.example.playlistmaker.main.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.search.ui.ComposeSearch
import com.example.playlistmaker.settings.ui.ComposeSettings
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel

@Composable
fun NavGraph(
    innerPadding: PaddingValues,
    navController: NavHostController,
    activityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel
) {
    Box(Modifier.padding(innerPadding)) {

        NavHost(
            navController = navController,
            startDestination = NavRoutes.Search.route
        ) {
            searchScreen(
                toSettingsClick = navController::navigateToSettings
            )
            settingsScreen(
                activityViewModel = activityViewModel,
                settingsViewModel = settingsViewModel,
                onBackClick = navController::popBackStack
            )
            mediaScreen(
                activityViewModel = activityViewModel,
                settingsViewModel = settingsViewModel,
                onBackClick = navController::popBackStack
            )
        }
    }
}

fun NavController.navigateToSettings(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = NavRoutes.Settings.route, navOptions)

fun NavGraphBuilder.settingsScreen(
    activityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel,
    onBackClick: () -> Unit
) {
    composable(route = NavRoutes.Settings.route) {
        ComposeSettings(
            singleActivityViewModel = activityViewModel,
            settingsViewModel = settingsViewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = NavRoutes.Search.route, navOptions)

fun NavGraphBuilder.searchScreen(
    toSettingsClick: () -> Unit
) {
    composable(route = NavRoutes.Search.route) {
        ComposeSearch(toSettingsClick)
    }
}

fun NavController.navigateToMedia(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = NavRoutes.Media.route, navOptions)

fun NavGraphBuilder.mediaScreen(
    activityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel,
    onBackClick: () -> Unit
) {
    composable(route = NavRoutes.Media.route) {
        ComposeSettings(
            singleActivityViewModel = activityViewModel,
            settingsViewModel = settingsViewModel,
            onBackClick = onBackClick
        )
    }
}