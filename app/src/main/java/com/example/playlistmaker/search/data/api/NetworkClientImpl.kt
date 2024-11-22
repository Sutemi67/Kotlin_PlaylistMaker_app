package com.example.playlistmaker.search.data.api

import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkClientImpl(
    private val itunesService: ITunesApi
) : NetworkClient {

    override suspend fun doRequestApi(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (dto is TracksSearchRequest) {
                    val response = itunesService.search(dto.expression)
                    response.apply { resultCode = 200 }
                } else {
                    Response(404)
                }
            } catch (e: Exception) {
                Log.e("NO_INTERNET", "NO INTERNET EXCEPTION")
                Response(400)
            }
        }
    }
}