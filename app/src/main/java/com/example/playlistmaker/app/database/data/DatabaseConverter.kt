package com.example.playlistmaker.app.database.data

import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.data.dto.TracksDTO
import com.example.playlistmaker.search.domain.models.Track
import java.util.Date

class DatabaseConverter {
    fun mapToListOfTracks(tracksFromDb: List<DatabaseEntityTrack>): List<Track> {
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
                it.isFavourite,
                it.latestTimeAdded
            )
        }
    }

    fun mapToTrackEntity(track: Track): DatabaseEntityTrack {
        val timeAdded = Date().time
        return DatabaseEntityTrack(
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
            true,
            timeAdded
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
                it.isFavourite,
                it.latestTimeAdded
            )
        }
    }

    fun mapToPlaylistEntity(playlist: Playlist): DatabaseEntityPlaylist {
        return DatabaseEntityPlaylist(
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            playlistTracks = playlist.tracks,
            playlistTracksCount = playlist.count,
            imagePath = playlist.coverUrl
        )
    }

    fun mapToPlaylist(playlist: List<DatabaseEntityPlaylist>): List<Playlist> {
        return playlist.map {
            Playlist(
                name = it.playlistName,
                description = it.playlistDescription,
                tracks = it.playlistTracks,
                count = it.playlistTracksCount,
                coverUrl = it.imagePath
            )
        }
    }
}