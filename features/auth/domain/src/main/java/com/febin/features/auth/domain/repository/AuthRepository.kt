package com.febin.features.auth.domain.repository

import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Domain interface for auth operations.
 * - Abstracts data sources (remote/local).
 * - Returns Result<User> for sync; Flow<Result<User>> for async/MVI.
 */
interface AuthRepository {
    suspend fun signin(email: String, password: String): Flow<Result<User>>  // MVI-friendly Flow
    suspend fun signup(fullName: String, email: String, password: String, phone: String, fellowship: String, role: String): Flow<Result<User>>

}