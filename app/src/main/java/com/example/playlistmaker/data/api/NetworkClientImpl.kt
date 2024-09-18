package com.example.playlistmaker.data.api

import android.util.Log
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
        return try {
            if (dto is TracksSearchRequest) {
                val response = itunesService.search(dto.expression).execute()
                response.body()?.apply { resultCode = response.code() } ?: Response(response.code())
            } else {
                return Response(404)
            }
        } catch (e: Exception) {
            Log.e("NO_INTERNET", "NO INTERNET EXCEPTION")
            Response(400)
        }
    }
}