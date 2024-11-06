package com.example.netweaver.ui.features.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.netweaver.R
import com.example.netweaver.ui.features.auth.components.Button
import com.example.netweaver.ui.features.auth.components.ClickButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val state = rememberTextFieldState()

    val uiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.text) {
        if (uiState.password != state.text) {
            viewModel.onEvent(LoginEvent.PasswordChanged(state.text.toString()))
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotBlank()) {
            delay(3000)
            viewModel.onEvent(LoginEvent.ClearError)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
            },
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.Start,
        ) {

            item {
                Icon(
                    painter = painterResource(R.drawable.app_name_logo),
                    contentDescription = "App Logo",
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .height(22.dp),
                )

                Spacer(modifier = Modifier.height(52.dp))
            }

            item {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "or",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiary
                        )

                        Text(
                            text = "Join NetWeaver",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.W600),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {

                ClickButton(
                    button = Button(
                        text = "Sign in with Google",
                        icon = painterResource(R.drawable.google_logo),
                        onClick = {}
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {


                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.25.dp
                    )

                    Text(
                        text = "or",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.25.dp
                    )

                }

                Spacer(modifier = Modifier.height(36.dp))


            }

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    BasicTextField(
                        value = uiState.email,
                        onValueChange = {
                            viewModel.onEvent(LoginEvent.EmailChanged(it))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        textStyle = MaterialTheme.typography.titleMedium.copy(MaterialTheme.colorScheme.onBackground),
                        cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onTertiary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        decorationBox = { innerTextField ->

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {

                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (uiState.email.isEmpty()) {
                                        Text(
                                            "Email or Phone",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onTertiary
                                        )
                                    }
                                    innerTextField()
                                }

                                if (uiState.email.isEmpty()) {

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        thickness = 1.dp
                                    )
                                } else {
                                    HorizontalDivider(
                                        color = if (uiState.emailError == null) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onError,
                                        thickness = 2.dp
                                    )

                                    if (uiState.emailError != null) {
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = uiState.emailError!!,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onError
                                        )
                                    }
                                }
                            }

                        }
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    BasicSecureTextField(
                        state = state,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        textObfuscationMode = if (passwordVisibility) TextObfuscationMode.Visible else TextObfuscationMode.Hidden,
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
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
                                                "Password",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onTertiary
                                            )
                                        }
                                        innerTextField()
                                    }

                                    Icon(
                                        painter = if (passwordVisibility) painterResource(R.drawable.visibility_on) else painterResource(
                                            R.drawable.visibility_off
                                        ),
                                        contentDescription = "Show/Hide Password",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.clickable {
                                            passwordVisibility = !passwordVisibility
                                        }
                                    )
                                }
                                if (state.text.isEmpty()) {

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        thickness = 1.dp
                                    )
                                } else {
                                    HorizontalDivider(
                                        color = if (uiState.passwordError == null) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onError,
                                        thickness = 2.dp
                                    )

                                    if (uiState.passwordError != null) {
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = uiState.passwordError!!,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onError
                                        )
                                    }
                                }
                            }

                        }
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))


            }

            item {

                Text(
                    "Forgot password?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    ClickButton(
                        button = Button(
                            text = "Continue",
                            onClick = {
                                viewModel.onEvent(LoginEvent.Login)
                            },
                            filled = true,
                            enabled = (!uiState.isLoading && uiState.emailError == null && uiState.passwordError == null && uiState.email.isNotBlank() && uiState.password.isNotBlank())
                        ),
                    )
                }

            }


        }

    }


}