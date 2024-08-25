package com.example.playlistmaker.data.dto

class TracksResponse(
    val resultCount: Int,
    val expression: String,
    val results: List<TracksDTO>
) : Response()