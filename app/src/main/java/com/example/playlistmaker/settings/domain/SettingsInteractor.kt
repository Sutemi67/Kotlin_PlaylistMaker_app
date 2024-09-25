package com.example.playlistmaker.settings.domain

import java.util.concurrent.Executors

class SettingsInteractor(
    private val repository: SettingsRepositoryInterface
) : SettingsInteractorInterface {

    private val executor = Executors.newCachedThreadPool()

    override fun openLinkAction() {
        executor.execute { repository.openLinkAction() }
    }

    override fun agreementAction() {
        executor.execute { repository.agreementAction() }
    }

    override fun shareAction() {
        executor.execute { repository.shareAction() }
    }

    override fun themeChangeAction(): Boolean {
        return repository.themeChangeAction()
    }
}