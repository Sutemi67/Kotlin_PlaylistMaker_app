package com.example.playlistmaker.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.app.ARTIST
import com.example.playlistmaker.app.ARTWORK_URL
import com.example.playlistmaker.app.COLLECTION_NAME
import com.example.playlistmaker.app.COUNTRY
import com.example.playlistmaker.app.GENRE
import com.example.playlistmaker.app.PREVIEW_URL
import com.example.playlistmaker.app.RELEASE_DATE
import com.example.playlistmaker.app.TRACK_NAME
import com.example.playlistmaker.app.TRACK_TIME_IN_MILLIS

@Entity(tableName = "tracks")
data class TrackDbEntity(
    @PrimaryKey val trackId: Int,
    @ColumnInfo(PREVIEW_URL) val previewUrl: String?,
    @ColumnInfo(TRACK_NAME) val trackName: String,
    @ColumnInfo(ARTIST) val artistName: String,
    @ColumnInfo(TRACK_TIME_IN_MILLIS) val trackTime: Int,
    @ColumnInfo(ARTWORK_URL) val artworkUrl100: String?,
    @ColumnInfo(COUNTRY) val country: String,
    @ColumnInfo(COLLECTION_NAME) val collectionName: String,
    @ColumnInfo(GENRE) val primaryGenreName: String,
    @ColumnInfo(RELEASE_DATE) val releaseDate: String?,
)