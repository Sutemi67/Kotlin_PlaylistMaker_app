package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.common.ARTIST
import com.example.playlistmaker.common.ARTWORK_URL
import com.example.playlistmaker.common.COLLECTION_NAME
import com.example.playlistmaker.common.COUNTRY
import com.example.playlistmaker.common.GENRE
import com.example.playlistmaker.common.PREVIEW_URL
import com.example.playlistmaker.common.RELEASE_DATE
import com.example.playlistmaker.common.TRACK_NAME
import com.example.playlistmaker.common.TRACK_TIME_IN_MILLIS
import com.google.gson.annotations.SerializedName

data class TracksDTO (
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
