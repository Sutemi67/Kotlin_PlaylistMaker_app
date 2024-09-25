package com.example.playlistmaker.settings.domain

import java.util.concurrent.Executors

class SettingsInteractor(
    private val repository: SettingsRepositoryInterface
) : SettingsInteractorInterface {

    private val executor = Executors.newCachedThreadPool()

    override fun settingsActivityOpenLinkAction() {
        executor.execute { repository.settingsActivityOpenLinkAction() }
    }

    override fun settingsActivityAgreementAction() {
        executor.execute { repository.settingsActivityAgreementAction() }
    }

    override fun settingsActivityShareAction() {
        executor.execute { repository.settingsActivityShareAction() }
    }
}