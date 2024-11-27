package com.example.netweaver.domain.repository

import com.example.netweaver.domain.model.User
import com.example.netweaver.ui.features.auth.AuthState
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val currentUserDetails: StateFlow<User?>

    suspend fun fetchUserDetails(): Result<Boolean>

    suspend fun registerWithEmail(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit>


    suspend fun signInWithEmail(email: String, password: String): Result<Unit>


    fun getAuthState(): Flow<AuthState>

    fun signOut()
}