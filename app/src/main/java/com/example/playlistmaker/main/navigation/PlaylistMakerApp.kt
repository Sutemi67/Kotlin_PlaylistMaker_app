package com.example.playlistmaker.main.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.app.MusicServiceConnection
import com.example.playlistmaker.app.PlayerService
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

    val serviceConnection = remember { MusicServiceConnection(playerViewModel) }
    val playerState by playerViewModel.observePlayerState().observeAsState()


    LaunchedEffect(Unit) {
        val intent = Intent(context, PlayerService::class.java).apply {
            putExtra("song_url", SONG_URL)
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        onDispose {
            context.unbindService(serviceConnection)
        }
    }

    PlaylistMakerTheme(
        darkTheme = darkModeState
    ) {
        PlaylistMakerScreen(
            playerViewModel = playerViewModel,
            navController = navController,
            activityViewModel = activityViewModel,
            settingsViewModel = settingsViewModel
        )
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
            if (currentDestination != "${NavRoutes.Player.route}/{trackJson}") {
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