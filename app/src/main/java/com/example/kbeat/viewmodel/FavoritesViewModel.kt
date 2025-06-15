package com.example.kbeat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kbeat.data.local.FavoriteSongEntity
import com.example.kbeat.data.repository.FavoritesRepository
import com.example.kbeat.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _favoriteSongs = MutableStateFlow<UiState<List<FavoriteSongEntity>>>(UiState.Loading)
    val favoriteSongs: StateFlow<UiState<List<FavoriteSongEntity>>> = _favoriteSongs

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            repository.getAllFavorites()
                .onStart {
                    _favoriteSongs.value = UiState.Loading
                }
                .catch { e ->
                    _favoriteSongs.value = UiState.Error(e.message ?: "Unknown error")
                }
                .collect { songs ->
                    delay(600) // Add small delay for better UX
                    _favoriteSongs.value = UiState.Success(songs)
                }
        }
    }


    fun addFavorite(song: FavoriteSongEntity) {
        viewModelScope.launch {
            repository.addFavorite(song)
            getFavorites() // refresh after add
        }
    }

    fun removeFavorite(fileName: String) {
        viewModelScope.launch {
            repository.removeFavorite(fileName)
            getFavorites() // refresh after remove
        }
    }
}


