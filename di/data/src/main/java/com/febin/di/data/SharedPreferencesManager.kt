package com.febin.di.data  // Declares the package for this file (organizes code in di module).

import android.content.Context  // Imports Android's Context class (provides app context for prefs).
import android.content.SharedPreferences  // Imports SharedPreferences interface (handles key-value storage).
import androidx.core.content.edit // Imports extension function for SharedPreferences.edit().

/**
 * Simple manager for SharedPreferences (e.g., for onboarding status, user role).
 * - Injected via Koin; use in Composables or ViewModels.
 * - Single source of truth for app-wide prefs.
 */  // KDoc comment explaining the class purpose and usage.

class SharedPreferencesManager(private val context: Context) {  // Defines the class; constructor takes Context (injected by Koin).

    companion object {  // Companion object for static-like constants (shared across instances).

        private const val PREF_NAME = "app_prefs"
        // Private constant: Name of the SharedPreferences file.
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        // Private constant: Key for onboarding flag.
        private const val KEY_CURRENT_ROLE = "user_role"
    // Private constant: Key for user role (e.g., "USER").

        // Add more keys as needed (e.g., theme_mode = "dark")  // Comment: Placeholder for future keys.

    }  // Ends companion object.

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    // Private val: Gets the SharedPreferences instance using context and file name (private mode).

    fun isOnboardingCompleted(): Boolean = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    // Public function: Retrieves boolean value for onboarding key (default false).

    fun setOnboardingCompleted(completed: Boolean) {  // Public function: Sets boolean value for onboarding key.

        prefs.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    // Gets editor, puts boolean, applies asynchronously.

    }  // Ends setOnboardingCompleted.

    fun getCurrentRole(): String? = prefs.getString(KEY_CURRENT_ROLE, null)
    // Public function: Retrieves string value for role key (default null).

    fun setCurrentRole(role: String?) {  // Public function: Sets string value for role key.

        prefs.edit { putString(KEY_CURRENT_ROLE, role) }
    // Gets editor, puts string, applies asynchronously.

    }  // Ends setCurrentRole.

    // Generic helpers (extend as needed)  // Comment: Section for reusable helpers.

    // Generic getter with reified type (inline for compile-time checks)
    private inline fun <reified T> getPref(key: String, default: T): T {
        return when (T::class) {  // Reified: Access T at compile-time
            String::class -> prefs.getString(key, default as String) as T
            Boolean::class -> prefs.getBoolean(key, default as Boolean) as T
            Int::class -> prefs.getInt(key, default as Int) as T
            Long::class -> prefs.getLong(key, default as Long) as T  // Now safe—no unchecked cast
            else -> default
        }
    }  // Ends getPref.

    fun putPref(key: String, value: Any) {  // Public function: Generic setter for any value type.

        prefs.edit {  // Gets editor and starts apply block.

            when (value) {  // When expression: Matches on value type to call correct put method.

                is String -> putString(key, value)  // If String, putString.

                is Boolean -> putBoolean(key, value)  // If Boolean, putBoolean.

                is Int -> putInt(key, value)  // If Int, putInt.

                is Long -> putLong(key, value)  // If Long, putLong.

            }  // Ends when.

        }  // Applies changes asynchronously.

    }  // Ends putPref.

    fun clearAll() {  // Public function: Clears all prefs.

        prefs.edit { clear() }  // Gets editor, clears all, applies.

    }  // Ends clearAll.

}  // Ends class.