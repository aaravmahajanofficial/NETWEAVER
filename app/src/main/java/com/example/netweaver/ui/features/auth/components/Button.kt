package com.example.netweaver.ui.features.auth.components

import androidx.compose.ui.graphics.painter.Painter

data class Button(
    val text: String,
    val icon: Painter? = null,
    val onClick: () -> Unit,
    val filled: Boolean? = false,
)
