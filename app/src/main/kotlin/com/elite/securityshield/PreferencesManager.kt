package com.elite.securityshield

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("security_shield_prefs")

class PreferencesManager(private val context: Context) {
    companion object {
        private val LAST_SCAN_TIME = longPreferencesKey("last_scan_time")
        private val IS_SECURE = booleanPreferencesKey("is_secure")
        private val SCAN_COUNT = intPreferencesKey("scan_count")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val AUTO_SCAN_INTERVAL = intPreferencesKey("auto_scan_interval")
    }

    fun getLastScanTime(): Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[LAST_SCAN_TIME] ?: 0L
    }

    fun getIsSecure(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_SECURE] ?: true
    }

    fun getScanCount(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[SCAN_COUNT] ?: 0
    }

    fun getNotificationsEnabled(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }

    fun getAutoScanInterval(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[AUTO_SCAN_INTERVAL] ?: 60 // Default: 60 minutes
    }

    suspend fun setLastScanTime(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SCAN_TIME] = time
        }
    }

    suspend fun setIsSecure(secure: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_SECURE] = secure
        }
    }

    suspend fun setScanCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[SCAN_COUNT] = count
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setAutoScanInterval(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_SCAN_INTERVAL] = minutes
        }
    }

    suspend fun incrementScanCount() {
        context.dataStore.edit { preferences ->
            val current = preferences[SCAN_COUNT] ?: 0
            preferences[SCAN_COUNT] = current + 1
        }
    }
}
