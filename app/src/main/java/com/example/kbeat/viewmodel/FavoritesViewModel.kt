package com.example.kbeat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kbeat.data.local.FavoriteSongEntity
import com.example.kbeat.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    val favoriteSongs: StateFlow<List<FavoriteSongEntity>> =
        repository.getAllFavorites().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFavorite(song: FavoriteSongEntity) {
        viewModelScope.launch {
            repository.addFavorite(song)
        }
    }

    fun removeFavorite(fileName: String) {
        viewModelScope.launch {
            repository.removeFavorite(fileName)
        }
    }
}

