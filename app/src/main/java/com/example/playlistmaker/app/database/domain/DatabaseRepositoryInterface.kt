package com.example.playlistmaker.app.database.domain

interface DatabaseRepositoryInterface {
    fun addTrackToFavourites()
    fun deleteTrackFromFavourites()
    fun getFavouritesList()
}