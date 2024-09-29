package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequestApi(dto: Any): Response
}