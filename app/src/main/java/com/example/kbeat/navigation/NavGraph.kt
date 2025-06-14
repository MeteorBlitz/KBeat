package com.example.kbeat.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kbeat.screens.home.HomeScreen
import com.example.kbeat.screens.player.PlayerScreen
import com.example.kbeat.screens.song_list.SongListScreen
import com.example.kbeat.screens.splash.SplashScreen
import com.example.kbeat.viewmodel.SharedSongViewModel

@Composable
fun KBeatNavGraph(navController: NavHostController) {
    val sharedSongViewModel: SharedSongViewModel = hiltViewModel()
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
            SongListScreen(category = category, navController = navController, sharedSongViewModel)
        }
        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("fileName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName") ?: ""

            PlayerScreen(
                fileName = fileName,
                sharedSongViewModel = sharedSongViewModel,
                navController = navController
            )
        }
    }
}