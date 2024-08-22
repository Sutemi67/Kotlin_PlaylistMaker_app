package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()

    var saveClickListener: SaveTrackInHistoryListener? = null
    var addingInHistoryLogicListener: AddInHistoryLogicListener? = null
    var openPlayerActivity: OpenPlayerActivity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list_item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            openPlayerActivity?.openPlayerActivity(tracks[position])
            addingInHistoryLogicListener?.savingLogic(position)
            saveClickListener?.saveTrackInHistory()
        }
    }

    override fun getItemCount(): Int = tracks.size

    interface SaveTrackInHistoryListener {
        fun saveTrackInHistory()
    }

    interface AddInHistoryLogicListener {
        fun savingLogic(position: Int)
    }

    interface OpenPlayerActivity {
        fun openPlayerActivity(track: Track)
    }

}