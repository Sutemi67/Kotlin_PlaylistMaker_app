package com.example.playlistmaker.app.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.playlistmaker.app.PLAYLIST_DESCRIPTION
import com.example.playlistmaker.app.PLAYLIST_NAME
import com.example.playlistmaker.app.PLAYLIST_PIC_PATH
import com.example.playlistmaker.app.PLAYLIST_TRACKLIST
import com.example.playlistmaker.app.PLAYLIST_TRACKS_COUNT

@Entity(
    tableName = "playlist",
    indices = [Index(value = ["playlist_name"], unique = true)]
)
data class DatabaseEntityPlaylist(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(PLAYLIST_NAME) val playlistName: String,
    @ColumnInfo(PLAYLIST_DESCRIPTION) val playlistDescription: String?,
    @ColumnInfo(PLAYLIST_PIC_PATH) val imagePath: String,
    @ColumnInfo(PLAYLIST_TRACKLIST) val playlistTracks: String,
    @ColumnInfo(PLAYLIST_TRACKS_COUNT) val playlistTracksCount: Int,
)
