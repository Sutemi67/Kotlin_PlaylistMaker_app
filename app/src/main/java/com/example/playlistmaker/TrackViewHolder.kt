package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
    }
}