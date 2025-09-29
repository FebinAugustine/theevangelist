package com.febin.core.data.local


import android.content.SharedPreferences
import androidx.core.content.edit

interface AppPreferences {
    fun saveAuthInfo(accessToken: String, refreshToken: String, userId: String, userType: String?)
    fun getToken(): String?
    fun getRefreshToken(): String?

    fun getAccessToken(): String?
    fun getUserId(): String?
    fun getUserType(): String?
    fun clearAuthInfo()
    fun saveTokens(accessToken: String, refreshToken: String?)
}

class AppPreferencesImpl(private val prefs: SharedPreferences) : AppPreferences {
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_TYPE = "user_type"
    }

    override fun saveAuthInfo(accessToken: String, refreshToken: String, userId: String, userType: String?) {
        val success = prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_TYPE, userType)
            .commit() // Use commit() to get a result
//        Timber.tag("PREFS_SAVE_RESULT").d("Auth info saved successfully: $success")
    }

    override fun getToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    override fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    override fun getUserType(): String? {
        return prefs.getString(KEY_USER_TYPE, null)
    }

    override fun clearAuthInfo() {
        prefs.edit(commit = true) {
            remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_TYPE)
        } // Use commit() for consistency
//        Timber.tag("PREFS_SAVE_RESULT").d("Auth info cleared successfully: $success")
    }

    override fun saveTokens(accessToken: String, refreshToken: String?) {
        prefs.edit(commit = true) {
            putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
        } // Use commit() for consistency
    }
}
