package com.febin.features.userdashboard.data.remote

import com.febin.features.userdashboard.data.dto.GetUserResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class UserApiServiceImpl(private val httpClient: HttpClient) : UserApiService {
    override suspend fun getCurrentUser(): GetUserResponseDto {
        val response = httpClient.get("user/me")
        if (response.status == HttpStatusCode.OK) {
            return response.body()
        } else {
            val errorBody = response.bodyAsText()
            throw Exception("Get current user failed: $errorBody")
        }
    }

    override suspend fun logout(): Any {
        val response = httpClient.post("user/logout")
        if (response.status == HttpStatusCode.OK) {
            return response.body<Any>()  // Success: Empty body
        } else {
            val errorBody = response.bodyAsText()
            throw Exception("Logout failed: $errorBody")
        }
    }
}