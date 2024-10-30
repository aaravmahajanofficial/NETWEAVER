package com.example.netweaver.data.repository

import com.example.netweaver.data.local.datastore.UserPreferences
import com.example.netweaver.data.local.datastore.UserPreferencesRepository
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImplementation(private val userPreferencesRepository: UserPreferencesRepository) :
    AuthRepository {

    override val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    override suspend fun login(email: String, password: String): Result<User> =

        withContext(Dispatchers.IO) {
            try {
                val response = TODO()

                userPreferencesRepository.saveUserPreferences(
                    UserPreferences(
                        userId = TODO(),
                        token = TODO(),
                        email = TODO()
                    )
                )

                // Would return the User object
                Result.Success(TODO())


            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun logout() {
        userPreferencesRepository.clearUserPreferences()
    }
}