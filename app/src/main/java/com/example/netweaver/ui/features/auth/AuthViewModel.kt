package com.example.netweaver.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    // retrieve the details from the UserPreferencesRepository/DataStore

    val isLoggedIn =
        authRepository.userPreferencesFlow.map { it != null }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), false
        )

    fun updateEmail(email: String) {

        _loginUiState.update { it.copy(email = email) }

    }

    fun updatePassword(password: String) {

        _loginUiState.update { it.copy(password = password) }

    }

    fun login() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _loginUiState.update { it.copy(isLoading = true, error = null) }

                when (val result =
                    authRepository.login(_loginUiState.value.email, _loginUiState.value.password)) {
                    is Result.Success -> {
                        _loginUiState.update { it.copy(isLoading = false, error = null) }
                    }

                    is Result.Error -> {
                        _loginUiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.toString()
                            )
                        }
                    }
                }

            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)