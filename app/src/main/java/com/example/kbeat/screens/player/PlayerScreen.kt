package com.example.kbeat.screens.player

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.kbeat.R
import com.example.kbeat.screens.components.KBeatTopBar
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun PlayerScreen(fileName: String, title: String, navController: NavController) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(getAssetUri(context, fileName))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    var isPlaying by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }

    val dominantColor = when {
        title.contains("LoFi", ignoreCase = true) -> Color(0xFF5F5CFF)
        title.contains("Night", ignoreCase = true) -> Color(0xFF9C27B0)
        title.contains("Retro", ignoreCase = true) -> Color(0xFFFF5722)
        title.contains("Heart", ignoreCase = true) -> Color(0xFFE91E63)
        title.contains("Morning", ignoreCase = true) -> Color(0xFFFFC107)
        else -> MaterialTheme.colorScheme.primary
    }

    val albumArt = when {
        title.contains("LoFi", ignoreCase = true) -> R.drawable.lofi
        title.contains("Night", ignoreCase = true) -> R.drawable.night_flow
        title.contains("Retro", ignoreCase = true) -> R.drawable.retro_song
        title.contains("Heart", ignoreCase = true) -> R.drawable.heart
        title.contains("Morning", ignoreCase = true) -> R.drawable.morning
        else -> R.drawable.retro // default
    }

    // Update progress every second
    LaunchedEffect(player) {
        while (isActive) {
            currentPosition = player.currentPosition
            duration = player.duration
            delay(1000L)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            dominantColor.copy(alpha = 0.85f),
                            Color.Black
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Favorite Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        // TODO: Save to Room DB
                    }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Album Art
                Image(
                    painter = painterResource(id = albumArt),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color.DarkGray)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Title & Artist
                Text(title, color = Color.White, fontSize = 20.sp)
                Text("Unknown Artist", color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(24.dp))

                // Seekbar
                Slider(
                    value = currentPosition.coerceAtLeast(0).toFloat(),
                    onValueChange = { player.seekTo(it.toLong()) },
                    valueRange = 0f..(duration.takeIf { it > 0 } ?: 1).toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.Gray
                    )
                )

                // Timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatTime(currentPosition), color = Color.White, fontSize = 12.sp)
                    Text(formatTime(duration), color = Color.White, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* TODO: prev song */ }) {
                        Icon(
                            Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = {
                        if (player.isPlaying) {
                            player.pause()
                            isPlaying = false
                        } else {
                            player.play()
                            isPlaying = true
                        }
                    }) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    IconButton(onClick = { /* TODO: next song */ }) {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

fun getAssetUri(context: Context, fileName: String): Uri {
    return Uri.parse("asset:///$fileName")
}

fun formatTime(ms: Long): String {
    val totalSecs = ms / 1000
    val mins = totalSecs / 60
    val secs = totalSecs % 60
    return String.format("%02d:%02d", mins, secs)
}
