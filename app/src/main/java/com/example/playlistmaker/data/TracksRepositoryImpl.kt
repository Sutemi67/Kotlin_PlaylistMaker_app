package com.example.playlistmaker.data

import com.example.playlistmaker.data.api.NetworkClient
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun refillTrackList(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return if (response.resultCode == 200) {
            (response as TracksResponse).results.map {
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
                    it.releaseDate
                )
            }
        } else {
            emptyList()
        }
    }
}
