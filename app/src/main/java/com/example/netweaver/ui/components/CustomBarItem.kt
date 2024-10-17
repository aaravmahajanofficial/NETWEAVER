package com.example.netweaver.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

data class BottomItem(
    val icon: Painter,
    val selectedIcon: Painter,
    val label: String,
    val isSelected: Boolean = false
)

// Combining icon + label
@Composable
fun CustomBarItem(
    item: BottomItem,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {

        Icon(
            painter = if (item.isSelected) item.selectedIcon else item.icon,
            contentDescription = null,
            tint = if (item.isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onTertiary
        )

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (item.isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onTertiary
        )

    }

}