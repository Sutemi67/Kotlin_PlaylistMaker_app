package com.example.playlistmaker.settings.domain

interface SettingsRepositoryInterface {
    fun openLinkAction()
    fun agreementAction()
    fun shareAction()
    fun themeChangeAction(): Boolean
}