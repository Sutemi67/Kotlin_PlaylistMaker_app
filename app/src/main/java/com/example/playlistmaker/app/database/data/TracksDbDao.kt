package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDbDao {

    @Query("SELECT*FROM tracks ORDER BY latestTime DESC")
    suspend fun getAllTracks(): List<DatabaseTrackEntity>

    @Query("SELECT*from tracks where trackId = :trackId")
    suspend fun getTrackById(trackId: Int): DatabaseTrackEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: DatabaseTrackEntity)

    @Delete
    suspend fun removeTrack(track: DatabaseTrackEntity)

    @Query("SELECT COUNT(*) FROM tracks")
    fun getTracksCount(): Int
}