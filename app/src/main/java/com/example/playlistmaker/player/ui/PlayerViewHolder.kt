package com.example.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist

class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistName =
        itemView.findViewById<TextView>(R.id.player_bottom_sheet_playlist_name)
    private val playlistCount =
        itemView.findViewById<TextView>(R.id.player_bottom_sheet_tracks_count)
    private val playlistCover =
        itemView.findViewById<ImageView>(R.id.player_bottom_sheet_playlist_cover)

    fun bind(model: Playlist) {
        playlistCount.text = when (model.count % 10) {
            0 -> "Нет треков"
            1 -> "${model.count} трек"
            in 2..4 -> "${model.count} трека"
            else -> "${model.count} треков"
        }
        playlistName.text = model.name

        val uri = model.coverUrl
        if (!uri.equals("null", ignoreCase = true)) {
            playlistCover.setImageURI(uri?.toUri())
        } else {
            playlistCover.setImageResource(R.drawable.img_placeholder)
        }
    }
}