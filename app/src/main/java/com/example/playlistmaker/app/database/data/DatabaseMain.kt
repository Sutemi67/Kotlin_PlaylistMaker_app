package com.example.playlistmaker.app.database.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DatabaseEntityTrack::class,
        DatabaseEntityPlaylist::class
    ],
    version = 6
)
abstract class DatabaseMain : RoomDatabase() {
    abstract fun tracksDbDao(): DaoTracks
    abstract fun playlistsDao(): DaoPlaylist
}