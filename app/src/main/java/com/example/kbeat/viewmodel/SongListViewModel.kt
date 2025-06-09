package com.example.kbeat.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kbeat.model.Song
import com.example.kbeat.model.sampleSongs
import com.example.kbeat.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SongListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _songs = MutableStateFlow<UiState<List<Song>>>(UiState.Loading)
    val songs: StateFlow<UiState<List<Song>>> = _songs

    private val category: String = savedStateHandle["category"] ?: "pop"

    init {
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch {
            _songs.value = UiState.Loading
            delay(600) // 1-second delay for loading effect

            try {
                val filteredSongs = sampleSongs.filter { category.lowercase() in it.categories }
                _songs.value = UiState.Success(filteredSongs)
            } catch (e: Exception) {
                _songs.value = UiState.Error("Failed to load songs")
            }
        }
    }
}