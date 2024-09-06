package com.example.playlistmaker.data.dto

class TracksResponse(val results: List<TracksDTO>, resultCode:Int) : Response(resultCode = resultCode)