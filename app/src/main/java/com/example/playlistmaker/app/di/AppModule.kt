package com.example.playlistmaker.app.di

import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.media.ui.FavouritesViewModel
import com.example.playlistmaker.media.ui.NewPlaylistViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<PlayerViewModel> { PlayerViewModel(get(), get()) }
    viewModel<FragmentSettingsViewModel> { FragmentSettingsViewModel(get()) }
    viewModel<SearchViewModel> { SearchViewModel(get()) }
    viewModel<SingleActivityViewModel> { SingleActivityViewModel(get()) }
    viewModel { FavouritesViewModel(get()) }
    viewModel { NewPlaylistViewModel(get()) }
}