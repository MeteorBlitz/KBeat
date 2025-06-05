package com.example.kbeat.screens.song_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kbeat.screens.components.KBeatTopBar

@Composable
fun SongListScreen(category: String,navController: NavController) {
    Scaffold(
        topBar = {
            KBeatTopBar(
                title = category,
                showBack = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Category: $category",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
