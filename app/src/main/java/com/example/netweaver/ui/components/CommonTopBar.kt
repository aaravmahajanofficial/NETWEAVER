package com.example.netweaver.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(modifier: Modifier = Modifier, title: String, showBack: Boolean = true) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                }
            } else null
        }
    )

}