package com.example.playlistmaker.media.ui.stateInterfaces

import com.example.playlistmaker.search.domain.models.Track

sealed interface TrackListState {
    data class Empty(val tracklist: List<Track>) : TrackListState
    data class Filled(val tracklist: List<Track>) : TrackListState
}