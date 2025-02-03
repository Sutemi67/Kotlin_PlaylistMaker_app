package com.example.playlistmaker.media.ui.stateInterfaces

sealed class PlayerState(
    val buttonState: Boolean,
    val progress: String
) {
    class Loading : PlayerState(false, "00:00")
    class Prepared : PlayerState(true, "00:00")
    class Playing(progress: String) : PlayerState(true, progress)
    class Paused(progress: String) : PlayerState(true, progress)
}