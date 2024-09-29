package com.example.playlistmaker.search.data.dto

class TracksResponse(
    val results: List<TracksDTO>,
    resultCode: Int
) : Response(resultCode)