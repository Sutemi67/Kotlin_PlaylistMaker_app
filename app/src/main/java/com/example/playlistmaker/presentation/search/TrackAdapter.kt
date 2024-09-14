package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter : ListAdapter<Track, TrackViewHolder>(TrackDiffUtilCallback()) {
    private var trackList: List<Track> = mutableListOf()

    var openPlayerActivity: OpenPlayerActivity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list_item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            openPlayerActivity?.openPlayerActivity(trackList[position])
        }
    }

    override fun getItemCount(): Int = trackList.size

    fun addTracksInList(tracks: List<Track>) {
        trackList = tracks
        submitList(tracks)
    }

    fun getTrackList(): List<Track> {
        return trackList
    }


    interface OpenPlayerActivity {
        fun openPlayerActivity(track: Track)
    }

}