package com.example.playlistmaker.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.recyclerView.TrackViewHolder.OnPlayClickListener
import com.example.playlistmaker.savings.SearchHistory


class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()
    var historyList = ArrayList<Track>()

    private val onClickListener = object : SearchHistory.OnTrackClickListener {
        override fun onTrackClick(holder: TrackViewHolder, position: Int) {

            if (historyList.size < 10) {
                for (i in 0..<historyList.size) {
                    if (tracks[position].trackId == historyList[i].trackId) {
                        historyList.removeAt(i)
                        historyList.add(0, tracks[position])
                        Toast.makeText(holder.itemView.context, "замена", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                historyList.add(0, tracks[position])
                Toast.makeText(holder.itemView.context, "добавлен в историю", Toast.LENGTH_SHORT)
                    .show()
            } else {
                for (i in 0..<historyList.size) {
                    if (tracks[position].trackId == historyList[i].trackId) {
                        historyList.removeAt(i)
                        historyList.add(0, tracks[position])
                        Toast.makeText(holder.itemView.context, "замена", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                historyList.removeAt(9)
                historyList.add(0, tracks[position])
                Toast.makeText(
                    holder.itemView.context,
                    "добавлен вместо 10 элемента",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    var onPlayClick: OnPlayClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list_item_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClickListener.onTrackClick(holder, position)
        }
        holder.onPlayClick = object : OnPlayClickListener {
            override fun onPlayClick() {
                onPlayClick?.onPlayClick()
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

}