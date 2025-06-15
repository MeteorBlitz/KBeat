package com.example.kbeat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.kbeat.model.Song
import com.example.kbeat.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SharedSongViewModel @Inject constructor() : ViewModel() {
    private var _songs: List<Song> = emptyList()

    fun setSongs(songs: List<Song>) {
        _songs = songs
    }

    fun getSongs(): List<Song> = _songs

    fun startPlayer(navController: NavController, songs: List<Song>, shuffle: Boolean = false) {
        val playList = if (shuffle) songs.shuffled() else songs
        val startFile = playList.firstOrNull()?.fileName ?: return

        setSongs(playList) // Set shuffled or ordered list
        navController.navigate(Screen.Player.passSong(startFile)) {
            popUpTo(Screen.Player.route) { inclusive = true }
            launchSingleTop = true
        }
    }

}
