package com.febin.features.auth.data.remote.services

import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.dto.SignupResponseDto
import com.febin.features.auth.data.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApiServiceImpl(private val httpClient: HttpClient) : AuthApiService {
    override suspend fun signup(request: SignupRequestDto): SignupResponseDto { // Return type changed
        return httpClient.post("auth/signup") {
            setBody(request)
        }.body<SignupResponseDto>() // Type argument changed
    }
}