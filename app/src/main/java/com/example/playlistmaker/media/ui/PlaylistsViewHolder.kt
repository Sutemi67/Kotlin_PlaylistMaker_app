package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.util.Log
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

    @SuppressLint("SetTextI18n")
    fun bind(model: Playlist) {
        name.text = model.name
        count.text = model.count.toString()

        val uri = model.coverUrl
        if (uri != "null") {
            Log.e("DATABASE", "загрузка картинки - $uri - как строка")
            try {
                cover.setImageURI(uri?.toUri())
            } catch (e: Exception) {
                Log.d("DATABASE", "загрузка картинки не удалась и вот почему:\n${e.message}")
            }
        } else {
            cover.setImageResource(R.drawable.img_placeholder)
            Log.e("DATABASE", "загрузка картинки - $uri - как нуль")
        }
    }
}

