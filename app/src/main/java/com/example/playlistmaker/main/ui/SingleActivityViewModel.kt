package com.example.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.domain.MainInteractorInterface

class SingleActivityViewModel(
    private val interactor: MainInteractorInterface
) : ViewModel() {

    fun setThemeValue() {
        when (interactor.getTheme()) {
            true -> {
                setDefaultNightMode(MODE_NIGHT_YES)
            }

            false -> {
                setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }
}