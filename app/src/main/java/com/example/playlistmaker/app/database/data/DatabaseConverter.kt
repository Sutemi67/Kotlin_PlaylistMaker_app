package com.example.playlistmaker.app.database.data

import android.util.Log
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.search.data.dto.TracksDTO
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        val trackList = Gson().toJson(playlist.tracks)
        return DatabaseEntityPlaylist(
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            playlistTracks = trackList,
            playlistTracksCount = playlist.tracks.size,
            imagePath = playlist.coverUrl
        )
    }

    fun mapToPlaylist(playlist: DatabaseEntityPlaylist): Playlist {
        val token = object : TypeToken<List<Track>>() {}.type
        val trackList: List<Track> = Gson().fromJson(playlist.playlistTracks, token) ?: emptyList()
        return Playlist(
            id = playlist.id,
            name = playlist.playlistName,
            description = playlist.playlistDescription,
            tracks = trackList,
            count = trackList.size,
            coverUrl = playlist.imagePath
        )
    }

    fun mapToPlaylistsList(playlist: List<DatabaseEntityPlaylist>): List<Playlist> {
        return playlist.map {
            val token = object : TypeToken<List<Track>>() {}.type
            val trackList: List<Track> = Gson().fromJson(it.playlistTracks, token) ?: emptyList()
            Log.d("DATABASE", "список треков в плейлисте - $trackList")
            Playlist(
                id = it.id,
                name = it.playlistName,
                description = it.playlistDescription,
                tracks = trackList,
                count = it.playlistTracksCount,
                coverUrl = it.imagePath
            )
        }
    }


}