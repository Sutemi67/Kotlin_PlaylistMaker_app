package com.example.playlistmaker.recyclerView

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("country") val country: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("releaseDate") val releaseDate: String,
)
