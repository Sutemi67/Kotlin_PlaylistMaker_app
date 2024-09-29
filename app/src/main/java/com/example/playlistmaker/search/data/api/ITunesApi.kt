package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.search.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TracksResponse>
}