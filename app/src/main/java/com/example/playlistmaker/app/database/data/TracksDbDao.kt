package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDbDao {

    @Query("SELECT*FROM tracks")
    fun getAllTracks(): List<TrackEntity>

    @Query("SELECT*from tracks where trackId LIKE :trackId")
    fun getTrackById(trackId: Int): TrackEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Delete
    fun deleteTrack(track: TrackEntity)
}