package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.settings.domain.OpenLinkUseCase

class SettingsViewModel(
    private val openLinkUseCase: OpenLinkUseCase,
    private val interactor: TracksInteractor,
    private val context: Context
) : ViewModel() {

    fun onSupportClick() {
        openLinkUseCase.execute()
    }
    fun onShareClick() {
//        shareUseCase.execute()
        interactor.shareClick(context)
    }
}