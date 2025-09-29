package com.febin.features.auth.domain.repository

import com.febin.core.domain.model.User

interface AuthRepository {
    suspend fun signup(
        fullname: String,
        email: String,
        password: String,
        phone: String,
        fellowship: String,
        role: String
    ): Result<User>

}
