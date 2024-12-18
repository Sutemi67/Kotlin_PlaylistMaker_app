package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist

class PlaylistsAdapter() : ListAdapter<Playlist, PlaylistsViewHolder>(PlaylistsDiffUtilCallback()) {
    private val difUtil = PlaylistsDiffUtilCallback()
    private val asyncListDiffer = AsyncListDiffer(this, difUtil)

    fun setData(list: List<Playlist>) = asyncListDiffer.submitList(list.toList())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout_playlist, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlaylistsViewHolder,
        position: Int
    ) {
        val currentList = asyncListDiffer.currentList
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
}