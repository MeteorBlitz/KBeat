package com.example.kbeat.screens.player

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kbeat.screens.components.KBeatTopBar
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PlayerScreen(fileName: String, title: String, navController: NavController) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val assetUri = getAssetUri(context, fileName)
            val mediaItem = MediaItem.fromUri(assetUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Scaffold(
        topBar = {
            KBeatTopBar(
                title = title,
                showBack = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        this.player = player
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

fun getAssetUri(context: Context, fileName: String): Uri {
    return Uri.parse("asset:///${fileName}")
}
