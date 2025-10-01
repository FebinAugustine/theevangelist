package com.febin.features.userdashboard.domain.repository

import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Flow<Result<User>>
    suspend fun logout(): Flow<Result<Unit>>


}