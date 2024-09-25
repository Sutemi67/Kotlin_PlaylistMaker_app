package com.example.playlistmaker.settings.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface

//вьюмодель ничего не должна возвращать!

class SettingsViewModel(
    private val interactor: SettingsInteractorInterface
) : ViewModel() {

    var isChecked: MutableLiveData<Boolean> = MutableLiveData()

    fun onShareClick() {
        interactor.shareAction()
    }

    fun onLinkClick() {
        interactor.openLinkAction()
    }

    fun onAgreementClick() {
        interactor.agreementAction()
    }

    fun onThemeCheckerClick() {
        isChecked.value = interactor.themeChangeAction()
    }
}