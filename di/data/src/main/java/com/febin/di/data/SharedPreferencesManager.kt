package com.febin.di.data

import android.content.Context  // Imports Android's Context class (provides app context for prefs).
import android.content.SharedPreferences  // Imports SharedPreferences interface (handles key-value storage).
import androidx.core.content.edit // Imports extension function for SharedPreferences.edit().
import timber.log.Timber // Import Timber

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
        private const val KEY_CURRENT_ROLE = "user_type" // Aligned key
    // Private constant: Key for user role (e.g., "USER").
        private const val TAG = "SharedPreferencesMgr"

    }  // Ends companion object.

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    // Private val: Gets the SharedPreferences instance using context and file name (private mode).

    init {
        Timber.tag(TAG).d("Initialized with prefs instance @${System.identityHashCode(prefs)} for file '$PREF_NAME'")
    }

    fun isOnboardingCompleted(): Boolean {
        val completed = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        Timber.tag(TAG).d("Reading isOnboardingCompleted from prefs instance @${System.identityHashCode(prefs)}: '$completed' for key '$KEY_ONBOARDING_COMPLETED'")
        return completed
    }

    fun setOnboardingCompleted(completed: Boolean) {  
        Timber.tag(TAG).d("Setting isOnboardingCompleted to '$completed' in prefs instance @${System.identityHashCode(prefs)} for key '$KEY_ONBOARDING_COMPLETED'")
        prefs.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    }  

    fun getCurrentRole(): String? {
        val role = prefs.getString(KEY_CURRENT_ROLE, null)
        Timber.tag(TAG).d("Reading getCurrentRole from prefs instance @${System.identityHashCode(prefs)}: '$role' for key '$KEY_CURRENT_ROLE'")
        return role
    }

    fun setCurrentRole(role: String?) {  
        Timber.tag(TAG).d("Setting getCurrentRole to '$role' in prefs instance @${System.identityHashCode(prefs)} for key '$KEY_CURRENT_ROLE'")
        prefs.edit { putString(KEY_CURRENT_ROLE, role) }
    }  

    private inline fun <reified T> getPref(key: String, default: T): T {
        return when (T::class) {  
            String::class -> prefs.getString(key, default as String) as T
            Boolean::class -> prefs.getBoolean(key, default as Boolean) as T
            Int::class -> prefs.getInt(key, default as Int) as T
            Long::class -> prefs.getLong(key, default as Long) as T  
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
        Timber.tag(TAG).d("Clearing all prefs from instance @${System.identityHashCode(prefs)}")
        prefs.edit { clear() }  
    }  

}  // Ends class.