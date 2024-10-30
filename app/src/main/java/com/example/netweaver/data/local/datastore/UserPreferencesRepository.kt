package com.example.netweaver.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val datastore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val TOKEN = stringPreferencesKey("token")
        val EMAIL = stringPreferencesKey("email")
    }

    // The map function is used to transform each emitted Preferences object into a new UserPreferences object, which is your custom data model
    val userPreferencesFlow: Flow<UserPreferences> = datastore.data.map { preferences ->
        UserPreferences(
            userId = preferences[PreferencesKeys.USER_ID] ?: "",
            email = preferences[PreferencesKeys.TOKEN] ?: "",
            token = preferences[PreferencesKeys.EMAIL] ?: ""
        )
    }

    suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userPreferences.userId
            preferences[PreferencesKeys.TOKEN] = userPreferences.token
            preferences[PreferencesKeys.EMAIL] = userPreferences.email
        }
    }

    suspend fun clearUserPreferences() {
        datastore.edit { it.clear() }
    }
}



