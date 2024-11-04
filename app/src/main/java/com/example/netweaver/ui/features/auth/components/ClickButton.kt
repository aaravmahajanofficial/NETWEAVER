package com.example.netweaver.ui.features.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClickButton(button: Button) {
    OutlinedButton(
        onClick = { button.onClick() },
        border = if (button.filled == false) BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.onBackground
        ) else null,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (button.filled == true) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
            contentColor = if (button.filled == true) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = button.enabled == true
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            button.icon?.let {
                Image(
                    painter = button.icon,
                    contentDescription = button.text,
                    modifier = Modifier
                        .height(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                button.text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.75.sp
                ),
            )
        }

    }
}
