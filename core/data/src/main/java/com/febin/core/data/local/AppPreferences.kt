package com.febin.core.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import timber.log.Timber // Import Timber

interface AppPreferences {
    fun saveUserInfo(userId: String, userType: String?)
    fun getUserId(): String?
    fun getUserType(): String?
    fun clearUserInfo()
}

class AppPreferencesImpl(private val prefs: SharedPreferences) : AppPreferences {
    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_TYPE = "user_type"
        private const val TAG = "AppPreferencesImpl"
    }

    override fun saveUserInfo(userId: String, userType: String?) {
        Timber.tag(TAG).d("Saving user info: prefs instance @${System.identityHashCode(prefs)}, userId='$userId', userType='$userType' for key '$KEY_USER_TYPE'")
        prefs.edit(commit = true) { 
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_TYPE, userType)
        }
        // Verify immediately after saving
        val savedRole = prefs.getString(KEY_USER_TYPE, "NOT_FOUND_IMMEDIATELY_AFTER_SAVE")
        Timber.tag(TAG).d("Verification read from prefs instance @${System.identityHashCode(prefs)}: userType for key '$KEY_USER_TYPE' is '$savedRole'")
    }

    override fun getUserId(): String? {
        val userId = prefs.getString(KEY_USER_ID, null)
        Timber.tag(TAG).d("Getting userId from prefs instance @${System.identityHashCode(prefs)}: '$userId' for key '$KEY_USER_ID'")
        return userId
    }

    override fun getUserType(): String? {
        val userType = prefs.getString(KEY_USER_TYPE, null)
        Timber.tag(TAG).d("Getting userType from prefs instance @${System.identityHashCode(prefs)}: '$userType' for key '$KEY_USER_TYPE'")
        return userType
    }

    override fun clearUserInfo() {
        Timber.tag(TAG).d("Clearing user info from prefs instance @${System.identityHashCode(prefs)} for keys '$KEY_USER_ID', '$KEY_USER_TYPE'")
        prefs.edit(commit = true) {
            remove(KEY_USER_ID)
            remove(KEY_USER_TYPE)
        }
    }
}
