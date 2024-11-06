package com.example.netweaver.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.repository.AuthRepository
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
        viewModelScope.launch {
            val currentUser = authRepository.isUserLoggedIn()
            _authState.value = AuthState.Success(currentUser)
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