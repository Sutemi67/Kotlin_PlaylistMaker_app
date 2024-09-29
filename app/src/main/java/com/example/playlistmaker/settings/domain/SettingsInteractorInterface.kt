package com.example.playlistmaker.settings.domain

interface SettingsInteractorInterface {
    fun openLinkAction()
    fun agreementAction()
    fun shareAction()
    fun themeChangeAction():Boolean
}