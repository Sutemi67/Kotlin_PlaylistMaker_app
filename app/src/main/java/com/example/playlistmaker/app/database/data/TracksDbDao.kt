package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDbDao {

    @Query("SELECT*FROM tracks")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT*from tracks where trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT COUNT(*) FROM tracks")
    fun getTracksCount(): Int
}