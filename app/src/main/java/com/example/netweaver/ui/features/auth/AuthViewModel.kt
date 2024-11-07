package com.example.netweaver.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val result = authRepository.fetchUserDetails()) {

                is Result.Success -> {
                    _authState.value = AuthState.Success
                }

                is Result.Error -> {
                    _authState.value = AuthState.Error(result.exception)
                }
            }
        }

    }

}

sealed interface AuthState {
    object Loading : AuthState
    data object Success : AuthState
    data class Error(val exception: Throwable) : AuthState
}