package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoPlaylist {

    @Query("SELECT*FROM playlist ORDER BY playlist_tracks_count DESC")
    suspend fun getPlaylists(): List<DatabaseEntityPlaylist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: DatabaseEntityPlaylist)

    @Delete
    suspend fun deletePlaylist(playlist: DatabaseEntityPlaylist)

    @Query("select*from playlist")
    suspend fun changePlaylist(): DatabaseEntityPlaylist
}