package com.example.playlistmaker.media.ui.observers

import com.example.playlistmaker.search.domain.models.Track

interface TrackLongClickListener {
    fun onTrackLongClick(track: Track)
}