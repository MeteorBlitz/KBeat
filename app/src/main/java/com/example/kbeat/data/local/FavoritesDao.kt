package com.example.kbeat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite_songs")
    fun getAllFavorites(): Flow<List<FavoriteSongEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_songs WHERE fileName = :fileName)")
    suspend fun isFavorite(fileName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(song: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE fileName = :fileName")
    suspend fun removeFavorite(fileName: String)
}