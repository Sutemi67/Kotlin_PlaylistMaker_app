package com.example.playlistmaker.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackDbEntity::class], version = 1)
abstract class TracksDb : RoomDatabase() {
    abstract fun tracksDbDao(): TracksDbDao

    companion object {
        @Volatile
        private lateinit var instance: TracksDb

        fun getInstance(context: Context): TracksDb {
            synchronized(this) {
                if (Companion::instance.isInitialized) {
                    instance = Room.databaseBuilder(
                        context,
                        TracksDb::class.java, "get-track-database"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}