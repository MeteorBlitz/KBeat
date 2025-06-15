package com.example.kbeat.model

data class PlayerState(
    val fileName: String = "",
    val title: String = "",
    val artist: String = "",
    val albumArtResId: Int = 0,
    val isPlaying: Boolean = true,
    val currentPosition: Long = 0L,
    val duration: Long = 0L
)