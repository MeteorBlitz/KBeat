package com.example.kbeat.data.repository

import com.example.kbeat.data.local.FavoriteSongEntity
import com.example.kbeat.data.local.FavoritesDao
import jakarta.inject.Inject

class FavoritesRepository @Inject constructor(
    private val dao: FavoritesDao
) {
    fun getAllFavorites() = dao.getAllFavorites()
    suspend fun isFavorite(fileName: String) = dao.isFavorite(fileName)
    suspend fun addFavorite(song: FavoriteSongEntity) = dao.addFavorite(song)
    suspend fun removeFavorite(fileName: String) = dao.removeFavorite(fileName)

}