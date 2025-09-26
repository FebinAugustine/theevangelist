package com.febin.di.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Simple manager for SharedPreferences (e.g., for onboarding status, user role).
 * - Injected via Koin; use in Composables or ViewModels.
 * - Single source of truth for app-wide prefs.
 */
class SharedPreferencesManager(private val context: Context) {
    companion object {
        private const val PREF_NAME = "app_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_CURRENT_ROLE = "user_role"  // e.g., "USER", "SUPER_ADMIN"
        // Add more keys as needed (e.g., theme_mode = "dark")
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isOnboardingCompleted(): Boolean = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    }

    fun getCurrentRole(): String? = prefs.getString(KEY_CURRENT_ROLE, null)

    fun setCurrentRole(role: String?) {
        prefs.edit { putString(KEY_CURRENT_ROLE, role) }
    }

    // Generic helpers (extend as needed)
    fun <T> getPref(key: String, default: T): T {
        return when (default) {
            is String -> prefs.getString(key, default) as T
            is Boolean -> prefs.getBoolean(key, default) as T
            is Int -> prefs.getInt(key, default) as T
            is Long -> prefs.getLong(key, default) as T
            else -> default
        }
    }

    fun putPref(key: String, value: Any) {
        prefs.edit {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
            }
        }
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
}