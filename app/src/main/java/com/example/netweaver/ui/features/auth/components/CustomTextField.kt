package com.example.netweaver.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String?,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Done,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        textStyle = MaterialTheme.typography.titleMedium.copy(
            MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp
        ),
        cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onTertiary),
        modifier = Modifier
            .fillMaxWidth(),
        decorationBox = { innerTextField ->

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W400
                            ),
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    innerTextField()
                }

                if (value.isEmpty() && error == null) {

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onTertiary,
                        thickness = 1.dp
                    )
                } else {
                    HorizontalDivider(
                        color = if (error == null) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onError,
                        thickness = 2.dp
                    )

                    if (error != null) {
                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }

        }
    )

}