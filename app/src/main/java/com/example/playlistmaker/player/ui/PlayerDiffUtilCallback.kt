package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.app.database.domain.model.Playlist

class PlayerDiffUtilCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(
        oldItem: Playlist,
        newItem: Playlist
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: Playlist,
        newItem: Playlist
    ): Boolean {
        return oldItem == newItem
    }
}