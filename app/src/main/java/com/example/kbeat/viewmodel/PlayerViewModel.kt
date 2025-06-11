package com.example.kbeat.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.kbeat.model.PlayerState
import com.example.kbeat.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlayerViewModel(context: Context, fileName: String) : ViewModel() {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        val mediaItem = MediaItem.fromUri(Uri.parse("asset:///$fileName"))
        setMediaItem(mediaItem)
        prepare()
        playWhenReady = true
    }

    private val _playerState = MutableStateFlow<UiState<PlayerState>>(UiState.Loading)
    val playerState: StateFlow<UiState<PlayerState>> = _playerState.asStateFlow()

    private var currentState = PlayerState()

    init {
        _playerState.value = UiState.Success(currentState.copy(duration = player.duration))

        viewModelScope.launch {
            while (true) {
                delay(1000L)
                val updatedState = currentState.copy(
                    isPlaying = player.isPlaying,
                    currentPosition = player.currentPosition,
                    duration = if (player.duration > 0) player.duration else 1L
                )
                currentState = updatedState
                _playerState.value = UiState.Success(updatedState)
            }
        }
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun releasePlayer() {
        player.release()
    }
}