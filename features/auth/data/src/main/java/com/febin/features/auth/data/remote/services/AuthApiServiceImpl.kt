package com.febin.features.auth.data.remote.services

import com.febin.core.domain.auth.TokenRefresher // ADDED: Import TokenRefresher
import com.febin.features.auth.data.dto.SigninRequestDto
import com.febin.features.auth.data.dto.SigninResponseDto
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.dto.SignupResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthApiServiceImpl(private val httpClient: HttpClient) : AuthApiService {


    override suspend fun signup(request: SignupRequestDto): SignupResponseDto {
        // Fixed: Direct DTO
        val response = httpClient.post("users/signup") {
            setBody(request)
        }
        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            return response.body<SignupResponseDto>()
        } else {
            val errorBody = response.bodyAsText()
            throw Exception("Signup failed: $errorBody")
        }
    }

    override suspend fun signin(request: SigninRequestDto): SigninResponseDto {
        // Fixed: Return DTO directly, no ApiResponse
        val response = httpClient.post("users/signin") {
            setBody(request)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SigninResponseDto>()  // Success: Parse body
        } else {
            val errorBody = response.bodyAsText()
            throw Exception("Login failed: $errorBody")  // Error: Throw for repo handling
        }
    }


}