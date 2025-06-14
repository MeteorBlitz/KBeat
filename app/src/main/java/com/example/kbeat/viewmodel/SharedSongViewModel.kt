package com.example.kbeat.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kbeat.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SharedSongViewModel @Inject constructor() : ViewModel() {
    private var _songs: List<Song> = emptyList()

    fun setSongs(songs: List<Song>) {
        _songs = songs
    }

    fun getSongs(): List<Song> = _songs
}
