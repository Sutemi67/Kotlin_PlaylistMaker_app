package com.example.playlistmaker.app.database.data

import com.example.playlistmaker.search.data.dto.TracksDTO
import com.example.playlistmaker.search.domain.models.Track

class TracksConverter {
    fun map(trackFromDb: TrackEntity): Track {
        return Track(
            trackFromDb.trackId,
            trackFromDb.previewUrl,
            trackFromDb.trackName,
            trackFromDb.artistName,
            trackFromDb.trackTime,
            trackFromDb.artworkUrl100,
            trackFromDb.country,
            trackFromDb.collectionName,
            trackFromDb.primaryGenreName,
            trackFromDb.releaseDate,
            false
        )
    }

    fun map(track: TracksDTO): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.previewUrl,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.collectionName,
            track.primaryGenreName,
            track.releaseDate,
            false
        )
    }
}