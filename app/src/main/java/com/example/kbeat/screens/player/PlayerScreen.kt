package com.example.kbeat.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kbeat.data.local.FavoriteSongEntity
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.FavoritesViewModel
import com.example.kbeat.viewmodel.PlayerViewModel
import com.example.kbeat.viewmodel.SharedSongViewModel
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(
    fileName: String,
    sharedSongViewModel: SharedSongViewModel,
    navController: NavController) {

    val context = LocalContext.current
    val songList = sharedSongViewModel.getSongs()

    if (songList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No songs found", color = Color.White)
        }
        return
    }

    val viewModel = remember { PlayerViewModel(context, songList, fileName) }
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val playerState by viewModel.playerState.collectAsState()
    val currentTitle by viewModel.currentSongTitle.collectAsState()
    val currentColor by viewModel.dominantColor.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentSong = playerState.let {
        if (it is UiState.Success) it.data else null
    }
    val isFavorite = favoritesViewModel.favoriteSongs.collectAsState().value
        .any { it.fileName == currentSong?.fileName }

    val dominantColor = currentColor

    DisposableEffect(Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            KBeatTopBar(
                title = currentTitle,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                backgroundColor = Color.Transparent,
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(dominantColor.copy(alpha = 0.85f), Color.Black)
                    )
                )
                .padding(padding)
        ) {
            when (val state = playerState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Text("Playback Error: ${state.message}")
                }
                is UiState.Success -> {
                    PlayerUI(
                        state = state.data,
                        onPlayPause = { viewModel.togglePlayPause() },
                        onSeek = { viewModel.seekTo(it) },
                        onPrevious = { viewModel.previous() },
                        onNext = { viewModel.next() },
                        isFavorite = isFavorite,
                        onFavoriteToggle = {
                            currentSong?.let { song ->
                                val favSong = FavoriteSongEntity(
                                    fileName = song.fileName,
                                    name = song.title,
                                    artist = song.artist ?: "Unknown",
                                    duration = song.duration.toString()
                                )
                                coroutineScope.launch {
                                    if (isFavorite) {
                                        favoritesViewModel.removeFavorite(song.fileName)
                                        snackbarHostState.showSnackbar("Removed from Favorites")
                                    } else {
                                        favoritesViewModel.addFavorite(favSong)
                                        snackbarHostState.showSnackbar("Added to Favorites")
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}