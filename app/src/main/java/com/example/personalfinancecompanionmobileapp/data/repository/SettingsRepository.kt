package com.example.personalfinancecompanionmobileapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        val CURRENCY = stringPreferencesKey("currency")
        val THEME = stringPreferencesKey("theme")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val NOTIFICATION_TIME_HOUR = stringPreferencesKey("notification_time_hour") // Keep simple, parse string
    }

    val currencyFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENCY] ?: "₹"
    }
    
    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME] ?: "System"
    }


    val biometricEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED] ?: false
    }

    val notificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_ENABLED] ?: false
    }

    val notificationTimeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_TIME_HOUR] ?: "20:00" // Default 8 PM
    }

    suspend fun saveCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY] =currency
        }

    }


    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME] =theme
        }
    }

    suspend fun toggleBiometric(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] =enabled
        }
    }

    suspend fun saveNotificationSettings(enabled: Boolean, time:String) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] =enabled
            preferences[NOTIFICATION_TIME_HOUR] =time
        }

    }


}
