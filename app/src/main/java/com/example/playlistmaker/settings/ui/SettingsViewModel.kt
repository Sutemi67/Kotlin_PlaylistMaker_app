package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface

class SettingsViewModel(
    private val interactor: SettingsInteractorInterface
) : ViewModel() {
    fun onShareClick() {
        interactor.settingsActivityShareAction()
    }

    fun onLinkClick() {
        interactor.settingsActivityOpenLinkAction()
    }

    fun onAgreementClick() {
        interactor.settingsActivityAgreementAction()
    }
}