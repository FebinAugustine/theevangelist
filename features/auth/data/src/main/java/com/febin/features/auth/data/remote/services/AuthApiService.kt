package com.febin.features.auth.data.remote.services

import com.febin.features.auth.data.dto.SigninRequestDto
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.dto.SignupResponseDto
import com.febin.features.auth.data.dto.SigninResponseDto



interface AuthApiService {
    suspend fun signup(request: SignupRequestDto): SignupResponseDto // Return type changed
    suspend fun signin(request: SigninRequestDto): Pair<SigninResponseDto, List<String>>
//    suspend fun refreshToken() // Added for HttpOnly cookie refresh
}
