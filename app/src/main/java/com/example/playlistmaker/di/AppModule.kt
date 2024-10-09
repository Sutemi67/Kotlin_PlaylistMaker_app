package com.example.playlistmaker.di

import com.example.playlistmaker.main.ui.MainViewModel
import com.example.playlistmaker.media.ui.FavouritesViewModel
import com.example.playlistmaker.media.ui.PlaylistViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<SettingsViewModel> { SettingsViewModel(get()) }
    viewModel<SearchViewModel> { SearchViewModel(get()) }
    viewModel<MainViewModel> { MainViewModel(get()) }
    viewModel<PlayerViewModel> { PlayerViewModel(get(), get()) }
    viewModel<PlaylistViewModel> { PlaylistViewModel() }
    viewModel<FavouritesViewModel> { FavouritesViewModel() }
}