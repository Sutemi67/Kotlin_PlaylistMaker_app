package com.example.playlistmaker.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.savings.Savings


class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()
    var historyList = ArrayList<Track>()

    private val onClickListener = object : Savings.OnTrackClickListener {

        override fun onTrackClick(holder: TrackViewHolder, position: Int) {

            if (historyList.size < 10) {
                if (historyList.isNotEmpty()) {
                    for (i in 0..<historyList.size) {
                        if (tracks[position].trackId == historyList[i].trackId) {
                            historyList.removeAt(i)
                            historyList.add(0, tracks[position])
                            Log.d("Adding", "Удален трек из истории с индексом $i и добавлен ${historyList[0]})")
                            return
                        }
                    }
                }
                Log.d(
                    "Adding",
                    "Дошли до добавления трека, размер массива истории ${historyList.size}, треклиста ${tracks.size}"
                )
                historyList.add(0, tracks[position])
                Log.d("Adding", "Меньше 10 треков список, добавлено без повторений")
            } else {
                for (i in 0..<historyList.size) {
                    if (tracks[position].trackId == historyList[i].trackId) {
                        historyList.removeAt(i)
                        historyList.add(0, tracks[position])
                        Log.d("Adding", "Замена")
                        return
                    }
                }
                historyList.removeAt(9)
                historyList.add(0, tracks[position])
                Log.d("Adding", "добавлен вместо 10 трека")
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
            onPlayClick?.onPlayClick()
        }
    }


    override fun getItemCount(): Int = tracks.size

    interface OnPlayClickListener {
        fun onPlayClick()
    }
}