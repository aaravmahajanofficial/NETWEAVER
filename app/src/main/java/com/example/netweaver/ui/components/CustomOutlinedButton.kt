package com.example.netweaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedButton(
    icon: Painter,
    buttonSize: Dp,
    borderColor: Color = MaterialTheme.colorScheme.onTertiary,
    iconColor: Color = MaterialTheme.colorScheme.tertiary,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .size(buttonSize)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
            )
            .border(
                1.dp,
                color = borderColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = icon,
            contentDescription = "",
            tint = iconColor
        )

    }
}

