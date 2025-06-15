package com.example.kbeat.screens.song_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kbeat.R
import com.example.kbeat.navigation.Screen
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.SharedSongViewModel
import com.example.kbeat.viewmodel.SongListViewModel

@Composable
fun SongListScreen(
    category: String,
    navController: NavController,
    sharedSongViewModel: SharedSongViewModel,
    viewModel: SongListViewModel = hiltViewModel()) {

    val songsState = viewModel.songs.collectAsState()

    val categoryImage = when (category.lowercase()) {
        "pop" -> R.drawable.pop
        "tamil" -> R.drawable.tamil
        "hindi" -> R.drawable.hindi
        "romance" -> R.drawable.romance
        "sad" -> R.drawable.sad
        "workout" -> R.drawable.workout
        "edm" -> R.drawable.edm
        "retro" -> R.drawable.retro
        else -> R.drawable.pop // fallback image
    }

    Scaffold(
        topBar = {
            KBeatTopBar(
                title = category,
                showBack = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        when (val state = songsState.value) {
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
                val songs = state.data
                sharedSongViewModel.setSongs(songs)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Header with Image + Title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = categoryImage),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = category,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }

                    // Play & Shuffle buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(onClick = {
                            sharedSongViewModel.startPlayer(navController, songs, shuffle = false)
                        }) {
                            Text("Play")
                        }
                        Button(onClick = {
                            sharedSongViewModel.startPlayer(navController, songs, shuffle = true)
                        }) {
                            Text("Shuffle")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Song list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(songs) { song ->
                            SongItem(
                                song = song,
                                onClick = {
                                    navController.navigate(
                                        Screen.Player.passSong(
                                            fileName = song.fileName
                                        )
                                    )
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
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red
                    )
                }
            }
        }
    }

}

