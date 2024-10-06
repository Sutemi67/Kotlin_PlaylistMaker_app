package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter : ListAdapter<Track, TrackViewHolder>(TrackDiffUtilCallback()) {
    var openPlayerActivity: OpenPlayerActivity? = null
    private val difUtil = TrackDiffUtilCallback()
    private val asyncListDiffer = AsyncListDiffer(this, difUtil)

    fun setData(list: List<Track>) {
        val l = list.toList()
        asyncListDiffer.submitList(l)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list_item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
        holder.itemView.setOnClickListener {
            openPlayerActivity?.openPlayerActivity(asyncListDiffer.currentList[position])
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

}