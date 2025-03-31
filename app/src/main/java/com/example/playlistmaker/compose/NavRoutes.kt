package com.example.playlistmaker.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(
    val name: String,
    val route: String,
    val isIcon: Boolean,
    val icon: ImageVector
) {

    object Settings : NavRoutes(
        name = "Настройки",
        route = "settings",
        isIcon = false,
        icon = Icons.Default.Settings
    )

    object Search : NavRoutes(
        name = "Поиск",
        route = "search",
        isIcon = false,
        icon = Icons.Default.Search
    )

    object Media : NavRoutes(
        name = "Медиа",
        route = "media",
        isIcon = false,
        icon = Icons.Default.PlayArrow
    )
    object Player : NavRoutes(
        name = "",
        route = "player",
        isIcon = true,
        icon = Icons.AutoMirrored.Filled.ArrowBack
    )
    object NewPlaylistPage : NavRoutes(
        name = "",
        route = "new_playlist",
        isIcon = true,
        icon = Icons.AutoMirrored.Filled.ArrowBack
    )

    object PlaylistDetails : NavRoutes(
        name = "",
        route = "playlist_details",
        isIcon = true,
        icon = Icons.AutoMirrored.Filled.ArrowBack
    )

    companion object {
        val bottomMenuItems by lazy { listOf(Search, Media, Settings) }
    }
}
