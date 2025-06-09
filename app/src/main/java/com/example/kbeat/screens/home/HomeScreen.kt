package com.example.kbeat.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kbeat.navigation.Screen
import com.example.kbeat.screens.components.KBeatTopBar
import com.example.kbeat.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()) {

    val categories = viewModel.categories.collectAsState(initial = emptyList())

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.value) { category ->
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
}


