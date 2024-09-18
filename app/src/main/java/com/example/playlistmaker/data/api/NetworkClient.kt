package com.example.playlistmaker.data.api

import com.example.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequestApi(dto: Any): Response
}