package com.example.playlistmaker.player.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist

class PlayerAdapter : ListAdapter<Playlist, PlayerViewHolder>(PlayerDiffUtilCallback()) {

    private val difUtil = PlayerDiffUtilCallback()
    private val asyncListDiffer = AsyncListDiffer(this, difUtil)
    var addTrackInPlaylist: AddingTrackInPlaylistInterface? = null

    fun setData(list: List<Playlist>) = asyncListDiffer.submitList(list.toList())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout_playlist_mini, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
        position: Int
    ) {
        val currentList = asyncListDiffer.currentList
        if (position in currentList.indices) {
            holder.bind(currentList[position])
            holder.itemView.setOnClickListener {
                addTrackInPlaylist?.addTrackInPlaylist(currentList[position])
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