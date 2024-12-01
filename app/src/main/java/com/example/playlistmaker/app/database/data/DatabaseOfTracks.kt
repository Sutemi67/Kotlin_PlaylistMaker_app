package com.example.playlistmaker.app.database.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseTrackEntity::class], version = 3)
abstract class DatabaseOfTracks : RoomDatabase() {
    abstract fun tracksDbDao(): TracksDbDao

}