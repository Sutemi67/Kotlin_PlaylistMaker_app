package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractorInterface

//вьюмодель ничего не должна возвращать!

class SettingsViewModel(
    private val interactor: SettingsInteractorInterface
) : ViewModel() {

    private var _isChecked: MutableLiveData<Boolean> = MutableLiveData()
    val isChecked: LiveData<Boolean> = _isChecked

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
        _isChecked.value = interactor.themeChangeAction()
    }
}