package com.example.netweaver.utils

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ExpandableText(
    text: String,
    maxLines: Int = 2
) {

    var isExpanded by remember { mutableStateOf(false) }
    var needsExpansion by remember { mutableStateOf(false) }

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            needsExpansion = textLayoutResult.hasVisualOverflow
        },
        modifier = Modifier.clickable {
            if (needsExpansion) {
                isExpanded = !isExpanded
            }
        }
    )

}