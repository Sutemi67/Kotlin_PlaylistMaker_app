package com.example.playlistmaker.app.database.data

import androidx.room.RoomDatabase
import com.example.playlistmaker.app.database.domain.DatabaseRepositoryInterface

class DatabaseRepository(
    private val database: RoomDatabase
) : DatabaseRepositoryInterface {
    override fun addTrackToFavourites() {

    }

    override fun deleteTrackFromFavourites() {
        TODO("Not yet implemented")
    }

    override fun getFavouritesList() {

    }
}