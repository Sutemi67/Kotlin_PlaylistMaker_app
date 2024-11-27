package com.example.playlistmaker.app.di

import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.FragmentSearchViewModel
import com.example.playlistmaker.settings.ui.FragmentSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<PlayerViewModel> { PlayerViewModel(get()) }
    viewModel<FragmentSettingsViewModel> { FragmentSettingsViewModel(get()) }
    viewModel<FragmentSearchViewModel> { FragmentSearchViewModel(get()) }
    viewModel<SingleActivityViewModel> { SingleActivityViewModel(get()) }
}