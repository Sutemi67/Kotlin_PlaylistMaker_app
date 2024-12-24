package com.example.playlistmaker.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
        Glide.with(itemView.context)
            .load(uri)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.img_placeholder)
            .into(cover)
    }
}

