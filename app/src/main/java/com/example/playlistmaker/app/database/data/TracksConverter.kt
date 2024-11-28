package com.example.playlistmaker.app.database.data

import com.example.playlistmaker.search.data.dto.TracksDTO
import com.example.playlistmaker.search.domain.models.Track

class TracksConverter {
    fun mapToListOfTracks(tracksFromDb: List<TrackEntity>): List<Track> {
        return tracksFromDb.map {
            Track(
                it.trackId,
                it.previewUrl,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.country,
                it.collectionName,
                it.primaryGenreName,
                it.releaseDate,
                false
            )
        }
    }

    fun mapToTrackEntity(track: Track): TrackEntity {
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

    fun mapToTracks(list: List<TracksDTO>): List<Track> {
        return list.map {
            Track(
                it.trackId,
                it.previewUrl,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.country,
                it.collectionName,
                it.primaryGenreName,
                it.releaseDate,
                false
            )
        }
    }
}