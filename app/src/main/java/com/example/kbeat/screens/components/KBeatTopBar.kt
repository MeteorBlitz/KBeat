package com.example.kbeat.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KBeatTopBar(
    title: String,
    showBack: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showBack && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            } else if (onMenuClick != null) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        }
    )
}