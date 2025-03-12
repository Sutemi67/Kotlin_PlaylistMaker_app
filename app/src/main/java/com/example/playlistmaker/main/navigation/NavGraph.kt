package com.example.playlistmaker.main.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.search.ui.ComposeSearch
import com.example.playlistmaker.settings.ui.ComposeSettings
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    activityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel
) {
    Box(
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Search.route
        ) {
            composable(route = NavRoutes.Search.route) {
                ComposeSearch {
                    navController.navigate(route = NavRoutes.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
            composable(route = NavRoutes.Media.route) {
                ComposeSettings(
                    singleActivityViewModel = activityViewModel,
                    settingsViewModel = settingsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(route = NavRoutes.Settings.route) {
                ComposeSettings(
                    singleActivityViewModel = activityViewModel,
                    settingsViewModel = settingsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}