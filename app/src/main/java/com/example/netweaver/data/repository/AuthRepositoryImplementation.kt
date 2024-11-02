package com.example.netweaver.data.repository

import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthRepository {

    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    override suspend fun registerWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser?> = try {
        withContext(Dispatchers.IO) {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(result.user)
        }
    } catch (e: Exception) {
        Result.Error(e)
    }


    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser?> = try {
        withContext(Dispatchers.IO) {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.Success(result.user)
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    override fun signOut() = firebaseAuth.signOut()


}