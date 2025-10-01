package com.febin.core.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import timber.log.Timber // Import Timber

interface AppPreferences {
    fun saveUserInfo(userEmail: String, userType: String?, userFullname: String? = null, userFellowship: String? = null, userPhone: String? = null)
    fun getUserEmail(): String?
    fun getUserType(): String?
    fun clearUserInfo()
    fun saveTokens(accessToken: String, refreshToken: String?)  // Added: Save tokens to prefs (fallback for HTTP-only)
    fun getToken(): String?  // Added: Get access token (from prefs or cookies)
    fun getRefreshToken(): String?  // Added: Get refresh token
}

class AppPreferencesImpl(private val prefs: SharedPreferences) : AppPreferences {
    companion object {
        private const val KEY_USER_EMAIL = "user_EMAIL"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_USER_FULLNAME = "user_fullname"
        private const val KEY_USER_FELLOWSHIP = "user_fellowship"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_ACCESS_TOKEN = "access_token"  // Fallback for HTTP-only
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val TAG = "AppPreferencesImpl"
    }

    override fun saveUserInfo(
        userEmail: String,
        userType: String?,
        userFullname: String?,
        userFellowship: String?,
        userPhone: String?
    ) {
        Timber.tag(TAG).d("Saving user info: prefs instance @${System.identityHashCode(prefs)}, userEmail='$userEmail', userType='$userType' for key '$KEY_USER_TYPE'")
        prefs.edit(commit = true) {
            putString(KEY_USER_EMAIL, userEmail)
            putString(KEY_USER_TYPE, userType)
            putString(KEY_USER_FULLNAME, userFullname)
            putString(KEY_USER_FELLOWSHIP, userFellowship)
            putString(KEY_USER_PHONE, userPhone)
        }
        // Verify immediately after saving
        val savedRole = prefs.getString(KEY_USER_TYPE, "NOT_FOUND_IMMEDIATELY_AFTER_SAVE")
        Timber.tag(TAG).d("Verification read from prefs instance @${System.identityHashCode(prefs)}: userType for key '$KEY_USER_TYPE' is '$savedRole'")
    }


    override fun getUserEmail(): String? {
        val userEmail = prefs.getString(KEY_USER_EMAIL, null)
        Timber.tag(TAG).d("Getting userId from prefs instance @${System.identityHashCode(prefs)}: '$userEmail' for key '$KEY_USER_EMAIL'")
        return userEmail
    }





    override fun getUserType(): String? {
        val userType = prefs.getString(KEY_USER_TYPE, null)
        Timber.tag(TAG).d("Getting userType from prefs instance @${System.identityHashCode(prefs)}: '$userType' for key '$KEY_USER_TYPE'")
        return userType
    }

    override fun clearUserInfo() {
        Timber.tag(TAG).d("Clearing user info from prefs instance @${System.identityHashCode(prefs)} for keys '$KEY_USER_EMAIL', '$KEY_USER_TYPE'")
        prefs.edit(commit = true) {
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_TYPE)
            remove(KEY_USER_FULLNAME)
            remove(KEY_USER_FELLOWSHIP)
            remove(KEY_USER_PHONE)
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_ACCESS_TOKEN)  // Clear token fallback
            remove(KEY_REFRESH_TOKEN)
        }
    }

    override fun saveTokens(accessToken: String, refreshToken: String?) {
        Timber.tag(TAG).d("Saving tokens to prefs: access='$accessToken.substring(0, 10)}...', refresh='${refreshToken?.substring(0, 10)}...'")
        prefs.edit(commit = true) {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    override fun getToken(): String? {
        val token = prefs.getString(KEY_ACCESS_TOKEN, null)
        Timber.tag(TAG).d("Getting access token from prefs: '${token?.substring(0, 10)}...'")
        return token
    }

    override fun getRefreshToken(): String? {
        val refreshToken = prefs.getString(KEY_REFRESH_TOKEN, null)
        Timber.tag(TAG).d("Getting refresh token from prefs: '${refreshToken?.substring(0, 10)}...'")
        return refreshToken
    }
}