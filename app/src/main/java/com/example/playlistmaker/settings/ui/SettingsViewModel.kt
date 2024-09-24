package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.TracksInteractor

class SettingsViewModel(
    private val interactor: TracksInteractor,
    private val context: Context
) : ViewModel() {

    fun onShareClick() {
        interactor.settingsActivityShareAction(context)
    }

    fun onLinkClick() {
        interactor.settingsActivityOpenLinkAction(context)
    }

    fun onAgreementClick() {
        interactor.settingsActivityAgreementAction(context)
    }
}