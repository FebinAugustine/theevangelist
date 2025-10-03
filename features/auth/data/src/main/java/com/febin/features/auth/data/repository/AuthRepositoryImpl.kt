package com.febin.features.auth.data.repository

import com.febin.core.data.local.AppPreferences
import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.features.auth.data.dto.SigninRequestDto
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.mapper.AuthMapper
import com.febin.features.auth.data.remote.services.AuthApiService
import com.febin.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/*
* Implementation of AuthRepository.
* - Combines remote (API) and local (preferences) sources.
* - Maps DTOs to domain; caches user details on success (tokens auto via HTTP-only cookies).
* - Returns Flow<Result<User>> for MVI.
*/
class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val appPreferences: AppPreferences
) : AuthRepository {

    override suspend fun signin(email: String, password: String): Flow<Result<User>> = flow {
        emit(Result.loading())

        try {
            // Call service which now returns body + Set-Cookie headers
            val (signinResponse, setCookieHeaders) = apiService.signin(SigninRequestDto(email, password))

            val cookieNamesAccess = listOf("access_token", "accessToken", "access-token")
            val cookieNamesRefresh = listOf("refresh_token", "refreshToken", "refresh-token")

            val accessToken = extractCookieValue(setCookieHeaders, cookieNamesAccess)
            val refreshToken = extractCookieValue(setCookieHeaders, cookieNamesRefresh)

            // Save tokens if present (log masked/length info)
            if (!accessToken.isNullOrBlank()) {
                Timber.d("signin: Access token found (len=${accessToken.length}). Saving to AppPreferences.")
                // Save tokens — ensure your AppPreferences.saveTokens exists
                appPreferences.saveTokens(accessToken, refreshToken ?: "")
            } else {
                Timber.w("signin: No access token found in Set-Cookie headers.")
            }

            // Map body to domain user
            val user = AuthMapper.toDomain(signinResponse)

            // Save other user info
            appPreferences.saveUserInfo(
                user.email,
                user.role,
                user.fullName,
                user.fellowship,
                user.phone
            )

            Timber.d("Signin successful for ${user.email}")
            emit(Result.success(user))
        } catch (e: Exception) {
            Timber.e(e, "Signin error")
            emit(Result.failure(Failure.fromException(e)))
        }
    }

    /**
     * Extract a cookie value from the list of Set-Cookie header strings.
     *
     * Example Set-Cookie header:
     * "accessToken=eyJ...; Path=/; HttpOnly; SameSite=Strict"
     */
    private fun extractCookieValue(setCookieHeaders: List<String>, cookieNameVariants: List<String>): String? {
        for (header in setCookieHeaders) {
            try {
                // Example header: "access_token=eyJ...; Max-Age=..., Path=/; HttpOnly"
                val cookiePair = header.split(';', limit = 2)[0].trim() // "access_token=eyJ..."
                val parts = cookiePair.split('=', limit = 2)
                if (parts.size == 2) {
                    val name = parts[0].trim()
                    val value = parts[1].trim()
                    // compare ignoring case for safety
                    if (cookieNameVariants.any { it.equals(name, ignoreCase = true) } && value.isNotEmpty()) {
                        return value
                    }
                }
            } catch (t: Throwable) {
                Timber.w(t, "Failed to parse Set-Cookie header: %s", header)
            }
        }
        return null
    }

//    override suspend fun signin(email: String, password: String): Flow<Result<User>> = flow {
//        emit(Result.loading())
//
//        try {
//            val response = apiService.signin(SigninRequestDto(email, password))  // Throws on error
//            val user = AuthMapper.toDomain(response)  // Direct mapping (no success check)
//            // Cache user details (tokens auto-saved in cookies by Ktor)
//            appPreferences.saveUserInfo(user.email, user.role, user.fullname, user.fellowship, user.phone)
//
//            Timber.d("Signin successful for ${user.email}")
//            emit(Result.success(user))
//        } catch (e: Exception) {
//            Timber.e(e, "Signin error")
//            emit(Result.failure(Failure.fromException(e)))  // Map exception to Failure
//        }
//    }

    override suspend fun signup(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        fellowship: String,
        role: String
    ): Flow<Result<User>> = flow {
        emit(Result.loading())

        try {
            val request = SignupRequestDto(fullName, fellowship, phone, email, password, role)
            val response = apiService.signup(request)  // Throws on error
            val user = AuthMapper.toDomain(response)
            // Cache user details (tokens auto via cookies)
            appPreferences.saveUserInfo(user.id, user.role)
            Timber.d("Signup successful for ${user.email}")
            emit(Result.success(user))
        } catch (e: Exception) {
            Timber.e(e, "Signup error")
            emit(Result.failure(Failure.fromException(e)))
        }
    }
}