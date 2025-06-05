package com.example.kbeat.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object SongList : Screen("song_list/{category}") {
        fun passCategory(category: String) = "song_list/$category"
    }
}
