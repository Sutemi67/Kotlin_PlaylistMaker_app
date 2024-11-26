package com.example.playlistmaker.app.database.domain

interface DatabaseInteractorInterface {
    fun addTrackToFavourites()
    fun deleteTrackFromFavourites()
    fun getFavouritesList()
}