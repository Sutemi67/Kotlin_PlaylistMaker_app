package com.example.playlistmaker.search.domain.models

import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Track) return false

        return trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId
    }
}
