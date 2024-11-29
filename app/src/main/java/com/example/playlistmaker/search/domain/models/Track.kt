package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.app.ARTIST
import com.example.playlistmaker.app.ARTWORK_URL
import com.example.playlistmaker.app.COLLECTION_NAME
import com.example.playlistmaker.app.COUNTRY
import com.example.playlistmaker.app.GENRE
import com.example.playlistmaker.app.PREVIEW_URL
import com.example.playlistmaker.app.RELEASE_DATE
import com.example.playlistmaker.app.TRACK_NAME
import com.example.playlistmaker.app.TRACK_TIME_IN_MILLIS
import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName(PREVIEW_URL) val previewUrl: String?,
    @SerializedName(TRACK_NAME) val trackName: String,
    @SerializedName(ARTIST) val artistName: String,
    @SerializedName(TRACK_TIME_IN_MILLIS) val trackTime: Int,
    @SerializedName(ARTWORK_URL) val artworkUrl100: String?,
    @SerializedName(COUNTRY) val country: String,
    @SerializedName(COLLECTION_NAME) val collectionName: String,
    @SerializedName(GENRE) val primaryGenreName: String,
    @SerializedName(RELEASE_DATE) val releaseDate: String?,
    var isFavourite: Boolean,
    val latestTimeAdded: Long
)
