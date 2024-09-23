package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.OpenLinkUseCase
import com.example.playlistmaker.settings.domain.ShareUseCase

class SettingsViewModel(
    private val openLinkUseCase: OpenLinkUseCase,
    private val shareUseCase: ShareUseCase
) : ViewModel() {

    fun onSupportClick() {
        openLinkUseCase.execute()
    }
    fun onShareClick() {
        shareUseCase.execute()
    }
}