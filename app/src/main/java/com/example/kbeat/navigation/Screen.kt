package com.example.kbeat.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object SongList : Screen("song_list/{category}") {
        fun passCategory(category: String) = "song_list/$category"
    }
    data object Player : Screen("player/{fileName}") {
        fun passSong(fileName: String): String {
            return "player/$fileName"
        }
    }
    object Favorites : Screen("favorites")
}
