package com.example.netweaver.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        val currentUser = authRepository.getCurrentUser()
        _authState.value = AuthState.Success(currentUser)
    }

    fun signInWithEmail(email: String, password: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val result = authRepository.signInWithEmail(email, password)) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data)
                }

                is Result.Error -> {
                    _authState.value = AuthState.Error(result.exception)
                }
            }

        }
    }

    fun registerWithEmail(email: String, password: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val result = authRepository.registerWithEmail(email, password)) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data)
                }

                is Result.Error -> {
                    _authState.value =
                        AuthState.Error(result.exception)
                }
            }

        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Success(null)
    }

}

sealed interface AuthState {
    object Loading : AuthState
    data class Success(val user: FirebaseUser?) : AuthState
    data class Error(val exception: Throwable) : AuthState
}