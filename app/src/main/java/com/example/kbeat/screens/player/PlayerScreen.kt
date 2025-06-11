package com.example.kbeat.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.kbeat.R
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(fileName: String, title: String, navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { PlayerViewModel(context, fileName) }
    val playerState by viewModel.playerState.collectAsState()

    val dominantColor = when {
        title.contains("LoFi", true) -> Color(0xFF5F5CFF)
        title.contains("Night", true) -> Color(0xFF9C27B0)
        title.contains("Retro", true) -> Color(0xFFFF5722)
        title.contains("Heart", true) -> Color(0xFFE91E63)
        title.contains("Morning", true) -> Color(0xFFFFC107)
        else -> MaterialTheme.colorScheme.primary
    }

    val albumArt = when {
        title.contains("LoFi", true) -> R.drawable.lofi
        title.contains("Night", true) -> R.drawable.night_flow
        title.contains("Retro", true) -> R.drawable.retro_song
        title.contains("Heart", true) -> R.drawable.heart
        title.contains("Morning", true) -> R.drawable.morning
        else -> R.drawable.retro
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    Scaffold(
        topBar = {
            KBeatTopBar(
                title = title,
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
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    Text("Playback Error: ${state.message}")
                }
                is UiState.Success -> {
                    PlayerUI(
                        state = state.data,
                        title = title,
                        albumArt = albumArt,
                        onPlayPause = { viewModel.togglePlayPause() },
                        onSeek = { viewModel.seekTo(it) }
                    )
                }
            }
        }
    }
}