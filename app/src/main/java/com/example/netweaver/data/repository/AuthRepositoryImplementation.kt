package com.example.netweaver.data.repository

import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.features.auth.AuthState
import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repository: Repository
) : AuthRepository {

    private val _currentUserDetails = MutableStateFlow<User?>(null)
    override val currentUserDetails: StateFlow<User?> = _currentUserDetails.asStateFlow()

    override suspend fun fetchUserDetails(): Result<Boolean> {

        val firebaseUser =
            firebaseAuth.currentUser ?: return Result.Error(Exception("User not logged in"))

        return when (val result = repository.getUserById(firebaseUser.uid)) {

            is Result.Success -> {
                _currentUserDetails.value = result.data
                Result.Success(true)
            }

            is Result.Error -> {
                _currentUserDetails.value = null
                Result.Error(result.exception)
            }
        }

    }


    override suspend fun registerWithEmail(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit> = withContext(Dispatchers.IO) {

        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            when (val response = repository.upsertUser(
                user = User(
                    userId = result.user?.uid!!,
                    email = result.user?.email!!,
                    firstName = firstName,
                    lastName = lastName
                )
            )) {
                is Result.Success -> {
                    _currentUserDetails.value = response.data
                    Result.Success(Unit)
                }

                is Result.Error -> {
                    result.user?.delete()?.await()
                    throw response.exception
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                when (fetchUserDetails()) {
                    is Result.Error -> {
                        Result.Error(Exception("Error fetching user details"))
                    }

                    is Result.Success -> {
                        Result.Success(Unit)
                    }
                }

            } ?: Result.Error(Exception("User does not exist"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getAuthState(): Flow<AuthState> = callbackFlow {

        trySend(AuthState.Loading)

        val authStateListener = FirebaseAuth.AuthStateListener { auth ->

            trySend(
                when {
                    auth.currentUser == null -> AuthState.Unauthenticated
                    else -> AuthState.Authenticated
                }
            )
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    override fun signOut() {
        firebaseAuth.signOut()
        _currentUserDetails.value = null
    }

}