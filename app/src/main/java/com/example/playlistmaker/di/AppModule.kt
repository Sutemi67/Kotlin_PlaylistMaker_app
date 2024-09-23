package com.example.playlistmaker.di

import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<SettingsViewModel> { SettingsViewModel(get(), get()) }
}