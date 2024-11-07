package com.example.netweaver.ui.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.usecase.user.SignInWithEmailUseCase
import com.example.netweaver.domain.usecase.validation.ValidateEmailUseCase
import com.example.netweaver.domain.usecase.validation.ValidatePasswordUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginState())
    val loginUiState: StateFlow<LoginState> = _loginUiState.asStateFlow()

    fun onEvent(event: LoginEvent) {

        when (event) {
            is LoginEvent.EmailChanged -> {

                _loginUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = validateEmail(event.email)
                    )
                }

            }

            is LoginEvent.PasswordChanged -> {

                _loginUiState.update {
                    it.copy(
                        password = event.password,
                        passwordError = validatePassword(event.password)
                    )
                }

            }

            is LoginEvent.Login -> {

                _loginUiState.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    try {
                        when (val result = signInWithEmailUseCase(
                            email = _loginUiState.value.email,
                            password = _loginUiState.value.password
                        )) {
                            is Result.Error -> {
                                _loginUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = result.exception.message
                                            ?: "Unknown error occurred"
                                    )
                                }
                            }

                            is Result.Success -> {
                                _loginUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        success = "Login successful"
                                    )
                                }
                            }
                        }

                    } catch (e: Exception) {
                        _loginUiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }


            is LoginEvent.ClearError -> {

                _loginUiState.update { it.copy(error = "") }

            }
        }


    }


}

data class LoginState(
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = ""
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object Login : LoginEvent()
    object ClearError : LoginEvent()
}