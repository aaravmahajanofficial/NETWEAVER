package com.example.netweaver.ui.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.usecase.RegisterWithEmailUseCase
import com.example.netweaver.domain.usecase.ValidateEmailUseCase
import com.example.netweaver.domain.usecase.ValidateNameUseCase
import com.example.netweaver.domain.usecase.ValidatePasswordUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validateName: ValidateNameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase
) :
    ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterState())
    val registerUiState: StateFlow<RegisterState> = _registerUiState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                _registerUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = validateEmail(event.email)
                    )
                }
            }

            is RegisterEvent.PasswordChanged -> {
                _registerUiState.update {
                    it.copy(
                        password = event.password,
                        passwordError = validatePassword(event.password)
                    )
                }
            }

            is RegisterEvent.FirstNameChanged -> {
                _registerUiState.update {
                    it.copy(
                        firstName = event.name,
                        firstNameError = validateName(event.name),
                    )
                }
            }

            is RegisterEvent.LastNameChanged -> {
                _registerUiState.update {
                    it.copy(
                        lastName = event.name,
                        lastNameError = validateName(event.name)
                    )
                }
            }

            is RegisterEvent.Register -> {

                _registerUiState.update { it.copy(isLoading = true) }

                viewModelScope.launch {

                    try {

                        when (val result = registerWithEmailUseCase(
                            email = _registerUiState.value.email,
                            password = _registerUiState.value.password,
                            firstName = _registerUiState.value.firstName,
                            lastName = _registerUiState.value.lastName
                        )) {
                            is Result.Success -> {
                                _registerUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        success = "Welcome to NetWeaver! Join the network and start weaving new connections."
                                    )
                                }
                            }

                            is Result.Error -> {
                                _registerUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = result.exception.message ?: "Unknown error occurred"
                                    )
                                }
                            }
                        }

                    } catch (e: Exception) {
                        _registerUiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Unknown error occurred"
                            )
                        }

                    }

                }

            }

            is RegisterEvent.ClearErrorsForNextPage -> {
                _registerUiState.update {
                    it.copy(
                        emailError = null,
                        passwordError = null,
                        firstNameError = null,
                        lastNameError = null
                    )
                }
            }

            is RegisterEvent.ClearError -> {
                _registerUiState.update { it.copy(error = null) }
            }


        }
    }


}

data class RegisterState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class FirstNameChanged(val name: String) : RegisterEvent()
    data class LastNameChanged(val name: String) : RegisterEvent()
    object Register : RegisterEvent()
    object ClearError : RegisterEvent()
    object ClearErrorsForNextPage : RegisterEvent()
}