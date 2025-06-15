package com.example.kbeat.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kbeat.data.local.FavoriteSongEntity
import com.example.kbeat.model.Song
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.FavoritesViewModel
import com.example.kbeat.viewmodel.PlayerViewModel
import com.example.kbeat.viewmodel.SharedSongViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(
    fileName: String,
    sharedSongViewModel: SharedSongViewModel,
    navController: NavController,
    playerViewModel: PlayerViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
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

    // Trigger only once when fileName changes
    LaunchedEffect(key1 = fileName) {
        playerViewModel.setSongsAndPlay(songList, fileName)
    }

    val playerState by playerViewModel.playerState.collectAsState()
    val currentTitle by playerViewModel.currentSongTitle.collectAsState()
    val currentColor by playerViewModel.dominantColor.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentSong = (playerState as? UiState.Success)?.data
    val favoritesState = favoritesViewModel.favoriteSongs.collectAsState().value

    val isFavorite = favoritesState is UiState.Success &&
            currentSong != null &&
            favoritesState.data.any { it.fileName == currentSong.fileName }

    // Release ExoPlayer on back press or recomposition destroy
    DisposableEffect(Unit) {
        onDispose {
            playerViewModel.releasePlayer()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            KBeatTopBar(
                title = currentTitle,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                backgroundColor = Color.Transparent
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(currentColor.copy(alpha = 0.85f), Color.Black)
                    )
                )
                .padding(paddingValues)
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Playback Error: ${state.message}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is UiState.Success -> {
                    PlayerUI(
                        state = state.data,
                        onPlayPause = { playerViewModel.togglePlayPause() },
                        onSeek = { playerViewModel.seekTo(it) },
                        onPrevious = { playerViewModel.previous() },
                        onNext = { playerViewModel.next() },
                        isFavorite = isFavorite,
                        onFavoriteToggle = {
                            currentSong?.let { song ->
                                val favSong = FavoriteSongEntity(
                                    fileName = song.fileName,
                                    name = song.title,
                                    artist = song.artist.ifEmpty { "Unknown" },
                                    duration = song.duration.toString()
                                )

                                coroutineScope.launch {
                                    if (isFavorite) {
                                        favoritesViewModel.removeFavorite(song.fileName)
                                        snackbarHostState.showSnackbar("Removed from Favorites")
                                        // Optional small delay
                                        delay(300)
                                        val updatedFavorites = favoritesViewModel.favoriteSongs.value
                                        if (updatedFavorites is UiState.Success) {
                                            val updatedList = updatedFavorites.data.mapIndexed { index, fav ->
                                                Song(
                                                    id = index + 1,
                                                    name = fav.name,
                                                    fileName = fav.fileName,
                                                    artist = fav.artist,
                                                    duration = fav.duration,
                                                    categories = listOf("Favorites")
                                                )
                                            }
                                            playerViewModel.updateSongList(updatedList)
                                        }
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
