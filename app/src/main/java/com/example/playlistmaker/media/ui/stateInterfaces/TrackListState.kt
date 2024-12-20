package com.example.playlistmaker.media.ui.stateInterfaces

import com.example.playlistmaker.search.domain.models.Track

sealed interface TrackListState {
    data object Empty : TrackListState
    data class Filled(val tracklist: List<Track>) : TrackListState
}