package com.febin.features.auth.data.remote.services

import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.dto.SignupResponseDto
import com.febin.features.auth.data.dto.UserDto


interface AuthApiService {
    suspend fun signup(request: SignupRequestDto): SignupResponseDto // Return type changed
}