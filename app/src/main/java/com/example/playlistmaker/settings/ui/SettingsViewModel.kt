package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface

//вьюмодель ничего не должна возвращать!

class SettingsViewModel(
    private val interactor: SettingsInteractorInterface
) : ViewModel() {

    fun onShareClick() = interactor.shareAction()
    fun onLinkClick() = interactor.openLinkAction()
    fun onAgreementClick() = interactor.agreementAction()
    fun getCheckerPos(): Boolean = interactor.getCheckerPos()

    fun onThemeCheckerClick() {
        when (interactor.getCheckerPos()) {
            true -> {
                setDefaultNightMode(MODE_NIGHT_NO)
                interactor.themeChangeClick()
            }

            else -> {
                setDefaultNightMode(MODE_NIGHT_YES)
                interactor.themeChangeClick()
            }
        }
    }
}