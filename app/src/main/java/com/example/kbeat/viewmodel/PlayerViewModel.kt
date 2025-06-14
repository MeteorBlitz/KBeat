package com.example.kbeat.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.kbeat.R
import com.example.kbeat.model.PlayerState
import com.example.kbeat.model.Song
import com.example.kbeat.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    context: Context,
    private val songList: List<Song>,
    startFileName: String
) : ViewModel() {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private var currentIndex = songList.indexOfFirst { it.fileName == startFileName }.coerceAtLeast(0)

    private val _playerState = MutableStateFlow<UiState<PlayerState>>(UiState.Loading)
    val playerState: StateFlow<UiState<PlayerState>> = _playerState.asStateFlow()

    private val _currentSongTitle = MutableStateFlow("")
    val currentSongTitle: StateFlow<String> = _currentSongTitle.asStateFlow()

    private val _dominantColor = MutableStateFlow(Color(0xFF5F5CFF)) // Default fallback
    val dominantColor: StateFlow<Color> = _dominantColor.asStateFlow()

    private var currentState = PlayerState()

    init {
        prepareAndPlay(currentIndex)

        viewModelScope.launch {
            while (true) {
                delay(1000)

                // Only update dynamic fields every second
                val updatedState = currentState.copy(
                    isPlaying = player.isPlaying,
                    currentPosition = player.currentPosition,
                    duration = if (player.duration > 0) player.duration else 1L
                )

                _playerState.value = UiState.Success(updatedState)
            }
        }

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    next()
                }
            }
        })
    }

    private fun prepareAndPlay(index: Int) {
        val song = songList.getOrNull(index) ?: return

        player.stop()
        player.clearMediaItems()

        player.setMediaItem(MediaItem.fromUri("asset:///${song.fileName}"))
        player.prepare()
        player.playWhenReady = true

        // Call getAlbumArt only once per song
        val albumArt = getAlbumArt(song.name)
        _currentSongTitle.value = song.name
        updateDominantColor(song.name)

        currentState = PlayerState(
            fileName = song.fileName,
            title = song.name,
            albumArtResId = albumArt,
            isPlaying = true,
            currentPosition = 0L,
            duration = if (player.duration > 0) player.duration else 1L
        )

        _playerState.value = UiState.Success(currentState)
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun next() {
        currentIndex = (currentIndex + 1) % songList.size
        prepareAndPlay(currentIndex)
    }

    fun previous() {
        currentIndex = if (currentIndex - 1 < 0) songList.lastIndex else currentIndex - 1
        prepareAndPlay(currentIndex)
    }

    fun releasePlayer() {
        player.release()
    }

    private fun getAlbumArt(title: String): Int {
        return when {
            title.contains("LoFi", true) -> R.drawable.lofi
            title.contains("Night", true) -> R.drawable.night_flow
            title.contains("Retro", true) -> R.drawable.retro_song
            title.contains("Heart", true) -> R.drawable.heart
            title.contains("Morning", true) -> R.drawable.morning
            else -> R.drawable.retro
        }
    }

    private fun updateDominantColor(title: String) {
        _dominantColor.value = when {
            title.contains("LoFi", true) -> Color(0xFF5F5CFF)
            title.contains("Night", true) -> Color(0xFF9C27B0)
            title.contains("Retro", true) -> Color(0xFFFF5722)
            title.contains("Heart", true) -> Color(0xFFE91E63)
            title.contains("Morning", true) -> Color(0xFFFFC107)
            else -> Color(0xFF607D8B) // Default: Blue Grey
        }
    }
}
