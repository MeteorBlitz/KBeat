package com.example.kbeat.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.PlayerViewModel
import com.example.kbeat.viewmodel.SharedSongViewModel

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
    val playerState by viewModel.playerState.collectAsState()
    val currentTitle by viewModel.currentSongTitle.collectAsState()
    val currentColor by viewModel.dominantColor.collectAsState()

    val dominantColor = currentColor

    DisposableEffect(Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    Scaffold(
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
                        onNext = { viewModel.next() }
                    )
                }
            }
        }
    }
}