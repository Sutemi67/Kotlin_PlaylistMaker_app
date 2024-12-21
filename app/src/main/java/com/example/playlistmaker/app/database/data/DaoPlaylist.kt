package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoPlaylist {

    @Query("SELECT*FROM playlist ORDER BY playlist_tracks_count DESC")
    suspend fun getPlaylists(): List<DatabaseEntityPlaylist>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createPlaylist(playlist: DatabaseEntityPlaylist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: DatabaseEntityPlaylist)

    @Query("DELETE FROM playlist WHERE playlist_name = :name")
    suspend fun deletePlaylist(name: String)

    @Query("select*from playlist where playlist_name = :playlistName ")
    suspend fun getPlaylist(playlistName: String): DatabaseEntityPlaylist
}