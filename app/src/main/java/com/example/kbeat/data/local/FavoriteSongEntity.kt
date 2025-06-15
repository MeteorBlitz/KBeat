package com.example.kbeat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey val fileName: String, // unique audio file
    val name: String,
    val artist: String,
    val duration: String
)