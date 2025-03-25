package com.example.playlistmaker.main.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.domain.MainInteractorInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SingleActivityViewModel(
    private val interactor: MainInteractorInterface
) : ViewModel() {

    private val _viewStates: MutableStateFlow<Boolean> = MutableStateFlow(false)
    fun viewStates(): StateFlow<Boolean> = _viewStates.asStateFlow()

    fun toggleTheme() {
        _viewStates.value = !_viewStates.value
        Log.i("theme", "Сменил тему во воюмодели на ${_viewStates.value}")
    }

    fun getThemeValue() {
        _viewStates.value = interactor.getTheme()
    }
}
