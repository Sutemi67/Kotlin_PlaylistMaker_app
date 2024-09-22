package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackListAndResponse
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun refillTrackList(expression: String): TrackListAndResponse {
        val response = networkClient.doRequestApi(TracksSearchRequest(expression))
        var trackList: List<Track> = emptyList()

        if (response.resultCode == 200) {
            (response as TracksResponse).results.map {
                trackList = response.results.map {
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
            }
            return TrackListAndResponse(trackList, response.resultCode)
        } else {
            return TrackListAndResponse(emptyList(), response.resultCode)
        }
    }
}
