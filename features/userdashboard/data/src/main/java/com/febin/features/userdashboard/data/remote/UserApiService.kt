package com.febin.features.userdashboard.data.remote

import com.febin.features.userdashboard.data.dto.GetUserResponseDto

interface UserApiService {
    suspend fun getCurrentUser(): GetUserResponseDto
    suspend fun logout(): Any

}