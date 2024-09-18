package com.example.playlistmaker.presentation.search

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.models.Track

class TrackDiffUtilCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}