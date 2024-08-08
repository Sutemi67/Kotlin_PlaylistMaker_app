package com.example.playlistmaker.recyclerView

import com.example.playlistmaker.ARTIST
import com.example.playlistmaker.ARTWORK_URL
import com.example.playlistmaker.COLLECTION_NAME
import com.example.playlistmaker.COUNTRY
import com.example.playlistmaker.GENRE
import com.example.playlistmaker.PREVIEW_URL
import com.example.playlistmaker.RELEASE_DATE
import com.example.playlistmaker.TRACK_NAME
import com.example.playlistmaker.TRACK_TIME_IN_MILLIS
import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Int,
    @SerializedName(PREVIEW_URL) val previewUrl: String,
    @SerializedName(TRACK_NAME) val trackName: String,
    @SerializedName(ARTIST) val artistName: String,
    @SerializedName(TRACK_TIME_IN_MILLIS) val trackTime: Int,
    @SerializedName(ARTWORK_URL) val artworkUrl100: String,
    @SerializedName(COUNTRY) val country: String,
    @SerializedName(COLLECTION_NAME) val collectionName: String,
    @SerializedName(GENRE) val primaryGenreName: String,
    @SerializedName(RELEASE_DATE) val releaseDate: String,
)
