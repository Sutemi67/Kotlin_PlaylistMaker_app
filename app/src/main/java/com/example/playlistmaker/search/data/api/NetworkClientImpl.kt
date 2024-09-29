package com.example.playlistmaker.search.data.api

import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class NetworkClientImpl(
    private val itunesService: ITunesApi
) : NetworkClient {

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