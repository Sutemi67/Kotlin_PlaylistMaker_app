package com.example.playlistmaker.app.database.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoPlaylist {

    @Query("SELECT*FROM playlist ORDER BY playlist_tracks_count DESC")
    suspend fun getPlaylists(): List<DatabaseEntityPlaylist>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createPlaylist(playlist: DatabaseEntityPlaylist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: DatabaseEntityPlaylist)

    @Update(entity = DatabaseEntityPlaylist::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist2(playlist: DatabaseEntityPlaylist)

    @Query("DELETE FROM playlist WHERE playlist_name = :name")
    suspend fun deletePlaylist(name: String)

    @Query("select*from playlist where id = :id ")
    suspend fun getPlaylist(id: Int?): DatabaseEntityPlaylist
}