package com.example.kbeat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSongEntity::class], version = 1)
abstract class KBeatDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}