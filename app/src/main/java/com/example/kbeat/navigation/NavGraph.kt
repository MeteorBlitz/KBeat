package com.example.kbeat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kbeat.screens.home.HomeScreen
import com.example.kbeat.screens.song_list.SongListScreen
import com.example.kbeat.screens.splash.SplashScreen

@Composable
fun KBeatNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.SongList.route) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Unknown"
            SongListScreen(category = category,navController)
        }

    }
}