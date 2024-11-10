package com.example.netweaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.netweaver.navigation.NavGraph
import com.example.netweaver.ui.theme.NETWEAVERTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch {
            keepSplashScreen = false
        }

        setContent {
            NETWEAVERTheme {
                NavGraph()
            }
        }
    }
}