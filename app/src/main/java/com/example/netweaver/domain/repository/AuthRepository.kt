package com.example.netweaver.domain.repository

import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    fun getCurrentUser(): FirebaseUser?
    suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?>
    fun signOut()
}