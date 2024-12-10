package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoTracks {

    @Query("SELECT*FROM tracks ORDER BY latestTime DESC")
    suspend fun getAllTracks(): List<DatabaseEntityTrack>

    @Query("SELECT*from tracks where trackId = :trackId")
    suspend fun getTrackById(trackId: Int): DatabaseEntityTrack

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: DatabaseEntityTrack)

    @Delete
    suspend fun removeTrack(track: DatabaseEntityTrack)

    @Query("SELECT COUNT(*) FROM tracks")
    fun getTracksCount(): Int
}