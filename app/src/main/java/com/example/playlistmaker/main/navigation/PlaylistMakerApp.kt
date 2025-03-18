package com.example.playlistmaker.main.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.compose.BottomNavBar
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaylistMakerApp() {

    val activityViewModel: SingleActivityViewModel = koinViewModel()
    val settingsViewModel: FragmentSettingsViewModel = koinViewModel()
    val playerViewModel: PlayerViewModel = koinViewModel()
    val context = LocalContext.current
    val navController = rememberNavController()
    val darkModeState: Boolean by activityViewModel.viewStates().collectAsState()

    PlaylistMakerTheme(
        darkTheme = darkModeState
    ) {
        Surface() {
            PlaylistMakerScreen(
                playerViewModel = playerViewModel,
                navController = navController,
                activityViewModel = activityViewModel,
                settingsViewModel = settingsViewModel
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaylistMakerScreen(
    navController: NavHostController,
    activityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel,
    playerViewModel: PlayerViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        bottomBar = {
            if (currentDestination != "${NavRoutes.Player.route}/{trackJson}" &&
                currentDestination != NavRoutes.NewPlaylistPage.route
            ) {
                BottomNavBar(navController)
            }
        }
    ) {
        NavGraph(
            navController = navController,
            activityViewModel = activityViewModel,
            settingsViewModel = settingsViewModel,
            playerViewModel = playerViewModel
        )
    }
}

@Preview
@Composable
fun Fdsf() {
}