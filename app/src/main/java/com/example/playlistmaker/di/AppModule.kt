package com.example.playlistmaker.di

import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.media.ui.FavouritesViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.FragmentSingleSearchViewModel
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<PlayerViewModel> { PlayerViewModel(get(), get()) }
    viewModel<FavouritesViewModel> { FavouritesViewModel() }
    viewModel<FragmentSettingsViewModel> { FragmentSettingsViewModel(get()) }
    viewModel<FragmentSingleSearchViewModel> { FragmentSingleSearchViewModel(get()) }
    viewModel<SingleActivityViewModel> { SingleActivityViewModel(get()) }
}