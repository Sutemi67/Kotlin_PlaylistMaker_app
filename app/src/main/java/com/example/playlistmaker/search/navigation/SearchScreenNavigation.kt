package com.example.playlistmaker.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.playlistmaker.search.ui.ComposeSearch
import kotlinx.serialization.Serializable

@Serializable
data object SearchScreenRoute

@Serializable
data object SearchScreenBaseRoute

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = SearchScreenRoute, navOptions)

fun NavGraphBuilder.searchScreen(
    toSettingsClick: () -> Unit
) {
    navigation<SearchScreenBaseRoute>(startDestination = SearchScreenRoute) {
        composable<SearchScreenRoute> { ComposeSearch(toSettingsClick) }
    }
}