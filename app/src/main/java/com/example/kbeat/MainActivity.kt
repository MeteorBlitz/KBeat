package com.example.kbeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.kbeat.navigation.KBeatNavGraph
import com.example.kbeat.ui.theme.KBeatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Your custom function to make content edge to edge

        setContent {
            KBeatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    KBeatAppUI()
                }
            }
        }
    }
}

@Composable
fun KBeatAppUI() {
    val navController = rememberNavController()
    KBeatNavGraph(navController)
}