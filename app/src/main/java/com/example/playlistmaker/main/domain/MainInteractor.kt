package com.example.playlistmaker.main.domain

class MainInteractor(
    private val repository: MainRepositoryInterface
) : MainInteractorInterface {
    override fun getTheme(): Boolean = repository.getTheme()
}