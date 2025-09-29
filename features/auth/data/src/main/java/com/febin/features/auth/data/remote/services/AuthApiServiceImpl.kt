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
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiServiceImpl(private val httpClient: HttpClient) : AuthApiService, TokenRefresher { // MODIFIED: Implement TokenRefresher
    override suspend fun signup(request: SignupRequestDto): SignupResponseDto {
        return httpClient.post("auth/signup") { // Assuming "auth/signup" path
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<SignupResponseDto>()
    }

    override suspend fun signin(request: SigninRequestDto): SigninResponseDto {
        return httpClient.post("auth/signin") { // Assuming "auth/signin" path
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<SigninResponseDto>()
    }

    override suspend fun refreshToken() {
        // Ktor engine automatically sends HttpOnly cookies.
        // The server is expected to set new HttpOnly cookies in the response headers.
        // expectSuccess = true in HttpClient will throw an exception for non-2xx responses.
        httpClient.post("refresh-token") { // Using "/refresh-token" as per your backend snippet
            // No explicit body typically needed if backend relies only on HttpOnly refresh token cookie
            // and sets new cookies in response headers. If an empty JSON body is required:
            // contentType(ContentType.Application.Json)
            // setBody("{}") 
        }
        // No explicit return value needed, success is indicated by no exception.
        // Ktor's engine handles storing the new HttpOnly cookies set by the server.
    }
}
