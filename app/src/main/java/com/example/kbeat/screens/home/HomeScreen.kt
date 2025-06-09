package com.example.kbeat.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kbeat.navigation.Screen
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.utils.UiState
import com.example.kbeat.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()) {

    val categoriesState = viewModel.categories.collectAsState()

    Scaffold(
        topBar = {
            KBeatTopBar(
                title = "KBeat",
                showBack = false,
                onMenuClick = {
                    // TODO: open drawer or show toast
                }
            )
        }
    ) { paddingValues ->
        when (val state = categoriesState.value) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 4.dp
                    )
                }
            }
            is UiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.data) { category ->
                        CategoryItem(
                            title = category.title,
                            categoryImageRes = category.imageRes,
                            backgroundColor = category.backgroundColor,
                            onClick = {
                                navController.navigate(Screen.SongList.passCategory(category.title))
                            }
                        )
                    }
                }
            }
            is UiState.Error -> {
                Text(text = "Error: ${state.message}")
            }
        }

    }
}


