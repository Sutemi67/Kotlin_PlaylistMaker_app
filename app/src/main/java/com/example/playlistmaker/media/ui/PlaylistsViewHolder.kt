package com.example.playlistmaker.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist

class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.album_name)
    private val count: TextView = itemView.findViewById(R.id.tracks_count)
    private val cover: ImageView = itemView.findViewById(R.id.playlist_cover)

    fun bind(model: Playlist) {
        name.text = model.name

        count.text = when (model.count % 10) {
            0 -> "Нет треков"
            1 -> "${model.count} трек"
            in 2..4 -> "${model.count} трека"
            else -> "${model.count} треков"
        }

        val uri = model.coverUrl
        if (!uri.equals("null", ignoreCase = true)) {
            cover.setImageURI(uri?.toUri())
        } else {
            cover.setImageResource(R.drawable.img_placeholder)
        }
    }
}

