package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequestApi(dto: Any): Response
}