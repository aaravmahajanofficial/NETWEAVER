package com.example.netweaver.ui.features.auth.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.netweaver.ui.features.auth.components.CustomSecureTextField
import com.example.netweaver.ui.features.auth.components.CustomTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel()) {

    val focusManager = LocalFocusManager.current
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val state = rememberTextFieldState()
    val pageState = rememberPagerState(initialPage = 0) { 2 }
    val scope = rememberCoroutineScope()

    val uiState by viewModel.registerUiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.text) {
        if (uiState.password != state.text) {
            viewModel.onEvent(RegisterEvent.PasswordChanged(state.text.toString()))
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            delay(3000)
            viewModel.onEvent(RegisterEvent.ClearError)
        }
    }

    BackHandler(enabled = pageState.currentPage > 0) {
        scope.launch {
            pageState.animateScrollToPage(pageState.currentPage - 1)
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
                        text = "Join NetWeaver",
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
                            text = "Sign in",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.W600),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(42.dp))
            }

            item {
                HorizontalPager(
                    state = pageState,
                    userScrollEnabled = false,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CustomTextField(
                                    value = uiState.email,
                                    onValueChange = {
                                        viewModel.onEvent(
                                            RegisterEvent.EmailChanged(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Email or Phone",
                                    error = uiState.emailError,
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                )

                                Spacer(modifier = Modifier.height(36.dp))

                                CustomSecureTextField(
                                    state = state,
                                    isPasswordVisible = passwordVisibility,
                                    onTogglePasswordVisibility = {
                                        passwordVisibility = !passwordVisibility
                                    },
                                    placeholder = "Password",
                                    error = uiState.passwordError,
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                )

                                Spacer(modifier = Modifier.height(36.dp))

                                Text(
                                    "By clicking Agree & Join or Continue, you agree to the NetWeaver User Agreement, Privacy Policy, and Cookie Policy. For phone number signups we will send a verification code via SMS.",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                ClickButton(
                                    button = Button(
                                        text = "Agree & Join",
                                        onClick = {
                                            if (uiState.email.isNotBlank() && uiState.password.isNotBlank() &&
                                                uiState.emailError == null && uiState.passwordError == null
                                            ) {
                                                scope.launch {
                                                    pageState.animateScrollToPage(1)
                                                }
                                            }
                                        },
                                        filled = true
                                    ),
                                )
                            }

                        }

                        1 -> {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CustomTextField(
                                    value = uiState.firstName,
                                    onValueChange = {
                                        viewModel.onEvent(
                                            RegisterEvent.FirstNameChanged(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "First name",
                                    error = uiState.firstNameError,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )

                                Spacer(modifier = Modifier.height(36.dp))

                                CustomTextField(
                                    value = uiState.lastName,
                                    onValueChange = {
                                        viewModel.onEvent(
                                            RegisterEvent.LastNameChanged(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Last name",
                                    error = uiState.lastNameError,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                )

                                Spacer(modifier = Modifier.height(42.dp))

                                ClickButton(
                                    button = Button(
                                        text = "Continue",
                                        onClick = {
                                            viewModel.onEvent(RegisterEvent.Register)
                                        },
                                        filled = true
                                    ),
                                )
                            }

                        }
                    }
                }

            }

            item {

                Spacer(modifier = Modifier.height(24.dp))

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

                Spacer(modifier = Modifier.height(24.dp))


            }

            item {
                ClickButton(
                    button = Button(
                        text = "Continue with Google",
                        icon = painterResource(R.drawable.google_logo),
                        onClick = {}
                    )
                )
            }

        }
    }

}