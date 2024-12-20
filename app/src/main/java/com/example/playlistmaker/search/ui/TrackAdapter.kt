package com.example.playlistmaker.search.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.observers.TrackLongClickListener
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter : ListAdapter<Track, TrackViewHolder>(TrackDiffUtilCallback()) {
    var openPlayerActivity: OpenPlayerActivity? = null
    var longClickAction: TrackLongClickListener? = null

    private val difUtil = TrackDiffUtilCallback()
    private val asyncListDiffer = AsyncListDiffer(this, difUtil)

    fun setData(list: List<Track>) = asyncListDiffer.submitList(list.toList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentList = asyncListDiffer.currentList
        if (position in currentList.indices) {
            holder.bind(currentList[position])
            holder.itemView.setOnClickListener {
                openPlayerActivity?.openPlayerActivity(currentList[position])
            }
            holder.itemView.setOnLongClickListener() {
                longClickAction?.onTrackLongClick()
                true
            }
        } else {
            Log.e(
                "DATABASE",
                "Invalid position: $position, current list size: ${asyncListDiffer.currentList.size}"
            )
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

}