package com.example.kbeat.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kbeat.model.Song
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.FavoritesViewModel
import com.example.kbeat.viewmodel.SharedSongViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    sharedSongViewModel: SharedSongViewModel,
    viewModel: FavoritesViewModel = hiltViewModel()

) {
    val state by viewModel.favoriteSongs.collectAsState()

    Scaffold(
        topBar = {
            KBeatTopBar(
                title = "Favorites",
                showBack = true,
                onBackClick = { navController.popBackStack() },
            )
        },
    ) { paddingValues ->
        when (state) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is UiState.Success -> {
                val favorites = (state as UiState.Success).data
                if (favorites.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No favorites yet", color = Color.White)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(favorites) { song ->
                            FavoriteItem(
                                song = song,
                                onRemove = { viewModel.removeFavorite(song.fileName) },
                                onClick = {
                                    val songs = favorites.mapIndexed { index, it ->
                                        Song(
                                            id = index + 1,
                                            name = it.name,
                                            fileName = it.fileName,
                                            artist = it.artist,
                                            duration = it.duration,
                                            categories = listOf("Favorites")
                                        )
                                    }
                                    sharedSongViewModel.startPlayer(navController, songs)
                                }
                            )

                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${(state as UiState.Error).message}", color = Color.Red)
                }
            }
        }
    }
}


