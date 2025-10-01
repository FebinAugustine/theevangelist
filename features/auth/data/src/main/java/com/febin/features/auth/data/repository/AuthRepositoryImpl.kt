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
            val response = apiService.signin(SigninRequestDto(email, password))  // Throws on error
            val user = AuthMapper.toDomain(response)  // Direct mapping (no success check)
            // Cache user details (tokens auto-saved in cookies by Ktor)
            appPreferences.saveUserInfo(user.email, user.role, user.fullname, user.fellowship, user.phone)

            Timber.d("Signin successful for ${user.email}")
            emit(Result.success(user))
        } catch (e: Exception) {
            Timber.e(e, "Signin error")
            emit(Result.failure(Failure.fromException(e)))  // Map exception to Failure
        }
    }

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