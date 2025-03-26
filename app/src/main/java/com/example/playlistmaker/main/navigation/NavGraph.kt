package com.example.playlistmaker.main.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.media.ui.ComposableMediaScreen
import com.example.playlistmaker.media.ui.NewPlaylistPage
import com.example.playlistmaker.media.ui.NewPlaylistViewModel
import com.example.playlistmaker.media.ui.PlaylistDetailsScreen
import com.example.playlistmaker.player.ui.ComposePlayerScreen
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.ComposeSearchScreen
import com.example.playlistmaker.settings.ui.ComposeSettingsScreen
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel
import java.net.URLDecoder

@Composable
fun NavGraph(
    navController: NavHostController,
    activityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel,
    playerViewModel: PlayerViewModel,
    newPlaylistViewModel: NewPlaylistViewModel
) {
    Box {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Media.route
        ) {
            composable(route = NavRoutes.Search.route) {
                ComposeSearchScreen(navController = navController)
            }
            composable(route = NavRoutes.Media.route) {
                ComposableMediaScreen(navController)
            }
            composable(route = NavRoutes.Settings.route) {
                ComposeSettingsScreen(
                    singleActivityViewModel = activityViewModel,
                    settingsViewModel = settingsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                route = "${NavRoutes.Player.route}/{trackJson}",
                arguments = listOf(navArgument("trackJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("trackJson") ?: ""
                val jsonTrack = URLDecoder.decode(encodedJson, "UTF-8")
                val track = JsonConverter.jsonToTrack(jsonTrack)
                ComposePlayerScreen(
                    playerViewModel = playerViewModel,
                    screenSettings = NavRoutes.Player,
                    track = track,
                    navHostController = navController
                )
            }
            composable(
                route = "${NavRoutes.NewPlaylistPage.route}/{playlistJson}",
                arguments = listOf(navArgument("playlistJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("playlistJson") ?: ""
                val jsonPlaylist = URLDecoder.decode(encodedJson, "UTF-8")
                val playlist = if (jsonPlaylist != "") {
                    JsonConverter.jsonToPlaylist(jsonPlaylist)
                } else {
                    null
                }
                NewPlaylistPage(
                    newPlaylistViewModel = newPlaylistViewModel,
                    navHostController = navController,
                    playlistForEdit = playlist
                )
            }
            composable(
                route = "${NavRoutes.PlaylistDetails.route}/{playlistJson}",
                arguments = listOf(navArgument("playlistJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("playlistJson") ?: ""
                val jsonPlaylist = URLDecoder.decode(encodedJson, "UTF-8")
                val playlist = JsonConverter.jsonToPlaylist(jsonPlaylist)
                PlaylistDetailsScreen(
                    navHostController = navController,
                    incomingPlaylist = playlist
                )
            }
        }
    }
}