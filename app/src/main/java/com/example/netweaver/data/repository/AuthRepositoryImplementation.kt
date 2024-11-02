package com.example.netweaver.data.repository

import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repository: Repository
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    override suspend fun isUserLoggedIn(): FirebaseUser? {
        fetchGetUserDetails()
        return firebaseAuth.currentUser
    }

    private suspend fun fetchGetUserDetails() {

        firebaseAuth.currentUser?.let {

            when (val result = repository.getUserById(it.uid)) {

                is Result.Success -> {
                    _currentUser.value = result.data
                }

                is Result.Error -> {
                    _currentUser.value = null
                }
            }

        }
    }


    override suspend fun registerWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser?> = try {
        withContext(Dispatchers.IO) {

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            when (val response = repository.upsertUser(
                user = User(
                    userId = result.user?.uid,
                    email = result.user?.email
                )
            )) {
                is Result.Success -> {
                    _currentUser.value = response.data
                    Result.Success(result.user)
                }

                is Result.Error -> {
                    result?.user?.delete()?.await()
                    throw response.exception
                }
            }
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser?> = try {

        if (email.isBlank() || password.isBlank()) {
            Result.Error(IllegalArgumentException("Email and password cannot be empty"))
        }

        withContext(Dispatchers.IO) {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            result.user?.let {
                when (val response = repository.getUserById(userId = it.uid)) {
                    is Result.Success -> {
                        _currentUser.value = response.data
                        Result.Success(result.user)
                    }

                    is Result.Error -> {
                        Result.Error(response.exception)
                    }
                }
            } ?: Result.Error(Exception("User not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    override fun signOut() {
        firebaseAuth.signOut()
        _currentUser.value = null
    }


}