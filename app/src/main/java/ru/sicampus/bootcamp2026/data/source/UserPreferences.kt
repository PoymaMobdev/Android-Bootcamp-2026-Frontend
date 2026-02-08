package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun saveUserAuth(token: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token
            preferences[KEY_USER_EMAIL] = email
        }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_EMAIL] = email
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token
        }
    }
    suspend fun getAccessToken(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[KEY_ACCESS_TOKEN]
    }
    suspend fun getUserEmail(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[KEY_USER_EMAIL]
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_ACCESS_TOKEN]
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}