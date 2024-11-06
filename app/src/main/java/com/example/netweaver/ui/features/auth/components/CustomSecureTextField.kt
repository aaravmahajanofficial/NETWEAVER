package com.example.netweaver.ui.features.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.netweaver.R

@Composable
fun CustomSecureTextField(
    state: TextFieldState,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    placeholder: String,
    error: String?,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Done
) {

    BasicSecureTextField(
        state = state,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        textObfuscationMode = if (isPasswordVisible) TextObfuscationMode.Visible else TextObfuscationMode.Hidden,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp
        ),
        cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onTertiary),
        modifier = Modifier.fillMaxWidth(),
        decorator = { innerTextField ->

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        if (state.text.isEmpty()) {
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

                    Icon(
                        painter = if (isPasswordVisible) painterResource(R.drawable.visibility_on) else painterResource(
                            R.drawable.visibility_off
                        ),
                        contentDescription = "Show/Hide Password",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable {
                            onTogglePasswordVisibility()
                        }
                    )
                }
                if (state.text.isEmpty() && error == null) {

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