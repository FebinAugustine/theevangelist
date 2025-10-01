package com.febin.features.userdashboard.data.repository

import com.febin.core.data.local.AppPreferences
import com.febin.features.userdashboard.domain.repository.UserRepository
import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.features.userdashboard.data.mapper.UserMapper
import com.febin.features.userdashboard.data.remote.UserApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val appPreferences: AppPreferences
): UserRepository {


    override suspend fun getCurrentUser(): Flow<Result<User>> = flow {
        emit(Result.loading())

        try {
            // Check local first
            val userEmail = appPreferences.getUserEmail()
            if (userEmail != null) {
                // Assume local fetch or API call with token (auto-sent via cookies)
                val response = userApiService.getCurrentUser()  // Implement in service
                val user = UserMapper.toDomain(response)
                Timber.d("User Fetched successful for ${user.email}")
                emit(Result.success(user))
            } else {
                emit(Result.failure(Failure.AuthError("No user found")))
            }
        } catch (e: Exception) {
            Timber.e(e, "Get current user error")
            emit(Result.failure(Failure.fromException(e)))
        }
    }

    override suspend fun logout(): Flow<Result<Unit>> {
        // implement logout
        return flow {
            emit(Result.loading())
            try {
                val response = userApiService.logout()
                appPreferences.clearUserInfo()
                Timber.d("Logout successful")
                emit(Result.success(Unit))
            } catch (e: Exception) {
                Timber.e(e, "Logout error")
                emit(Result.failure(Failure.fromException(e)))
            }
        }
    }
}