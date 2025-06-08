package com.example.kbeat.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kbeat.R
import com.example.kbeat.navigation.Screen
import com.example.kbeat.screens.components.KBeatTopBar

@Composable
fun HomeScreen(navController: NavController) {
    val categories = listOf(
        Triple("Pop", R.drawable.pop, Color(0xFFFF7043)),
        Triple("Tamil", R.drawable.tamil, Color(0xFF6A1B9A)),
        Triple("Hindi", R.drawable.hindi, Color(0xFF4CAF50)),
        Triple("Romance", R.drawable.romance, Color(0xFFD81B60)),
        Triple("Sad", R.drawable.sad, Color(0xFF37474F)),
        Triple("Workout", R.drawable.workout, Color(0xFFFFC107)),
        Triple("EDM", R.drawable.edm, Color(0xFF7E57C2)),
        Triple("Retro", R.drawable.retro, Color(0xFF607D8B))
    )

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
            items(categories) { (title, imgRes,bgColor)  ->
                CategoryItem(
                    title = title,
                    categoryImageRes = imgRes,
                    backgroundColor = bgColor,
                    onClick = {
                        navController.navigate(Screen.SongList.passCategory(title))
                    }
                )
            }
        }
    }
}


