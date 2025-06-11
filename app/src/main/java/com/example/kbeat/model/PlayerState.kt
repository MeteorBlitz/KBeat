package com.example.kbeat.model

data class PlayerState(
    val isPlaying: Boolean = true,
    val currentPosition: Long = 0L,
    val duration: Long = 1L
)