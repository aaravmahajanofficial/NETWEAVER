package com.example.netweaver.domain.repository

import com.example.netweaver.data.local.datastore.UserPreferences
import com.example.netweaver.domain.model.User
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    val userPreferencesFlow: Flow<UserPreferences?>
}