package com.example.playlistmaker.main.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.main.domain.MainInteractorInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class SingleActivityViewModel(
    private val interactor: MainInteractorInterface
) : ViewModel() {

    var data = true

    val result: Flow<Boolean> = flow {
        emit(data)
        Log.i("theme", "flow started")

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = true
    )

    fun toggleTheme() {
        data = !data
        Log.i("theme", "Сменил тему во воюмодели на $data")
    }

//    fun setThemeValue() {
//        when (interactor.getTheme()) {
//            true -> {
//                setDefaultNightMode(MODE_NIGHT_YES)
//            }
//
//            false -> {
//                setDefaultNightMode(MODE_NIGHT_NO)
//            }
//        }
//    }

}

