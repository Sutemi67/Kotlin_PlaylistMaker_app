package com.example.playlistmaker.data.api

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequestApi(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = itunesService.search(dto.expression).execute()
            val body = resp.body() ?: Response(resp.code())
            return body
        } else {
            return Response(400)
        }
    }
}