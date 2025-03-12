package com.example.playlistmaker.compose

import androidx.compose.material.icons.Icons
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

    companion object {
        val items by lazy { listOf(Search, Media, Settings) }
    }
}
