package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface

class FragmentSettingsViewModel(
    private val interactor: SettingsInteractorInterface
) : ViewModel() {

    fun onShareClick() = interactor.shareAction()
    fun onLinkClick() = interactor.openLinkAction()
    fun onAgreementClick() = interactor.agreementAction()

    fun getCheckerPos(): Boolean = interactor.getCheckerPos()
    fun onThemeCheckerClick() = interactor.themeChangeClick()

}
