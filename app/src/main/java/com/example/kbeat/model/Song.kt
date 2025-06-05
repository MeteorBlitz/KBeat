package com.example.kbeat.model

data class Song(
    val id: Int,
    val name: String,
    val artist: String,
    val duration: String,
    val fileName: String // for SoundHelix-Song-1.mp3 etc.
)
