package com.febin.features.auth.data.remote.services

import com.febin.features.auth.data.dto.ApiErrorResponseDto
import com.febin.features.auth.data.dto.SigninRequestDto
import com.febin.features.auth.data.dto.SigninResponseDto
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.dto.SignupResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class AuthApiServiceImpl(private val httpClient: HttpClient) : AuthApiService {

    private val json = Json { ignoreUnknownKeys = true } // For lenient parsing

    override suspend fun signup(request: SignupRequestDto): SignupResponseDto {
        val response = httpClient.post("auth/signup") {
            setBody(request)
        }
        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            return response.body<SignupResponseDto>()
        } else {
            val errorBody = response.bodyAsText()
            val errorDto = try {
                json.decodeFromString<ApiErrorResponseDto>(errorBody)
            } catch (e: Exception) {
                null
            }
            val message = errorDto?.message ?: errorDto?.error ?: "Signup failed"
            throw Exception(message)
        }
    }

//    override suspend fun signin(request: SigninRequestDto): SigninResponseDto {
//        val response = httpClient.post("auth/signin") {
//            setBody(request)
//        }
//        if (response.status == HttpStatusCode.OK) {
//            return response.body<SigninResponseDto>()
//        } else {
//            val errorBody = response.bodyAsText()
//            val errorDto = try {
//                json.decodeFromString<ApiErrorResponseDto>(errorBody)
//            } catch (e: Exception) {
//                null
//            }
//            val message = errorDto?.message ?: errorDto?.error ?: "Invalid credentials"
//            throw Exception(message)
//        }
//    }

    override suspend fun signin(request: SigninRequestDto): Pair<SigninResponseDto, List<String>> {
        val response = httpClient.post("auth/signin") {
            setBody(request)
        }

        // collect Set-Cookie headers (may be multiple)
        val setCookieHeaders: List<String> = response.headers.getAll(HttpHeaders.SetCookie) ?: emptyList()

        if (response.status == HttpStatusCode.OK) {
            val body = response.body<SigninResponseDto>()
            return Pair(body, setCookieHeaders)
        } else {
            val errorBody = response.bodyAsText()
            val errorDto = try {
                json.decodeFromString<ApiErrorResponseDto>(errorBody)
            } catch (e: Exception) {
                null
            }
            val message = errorDto?.message ?: errorDto?.error ?: "Invalid credentials"
            throw Exception(message)
        }
    }
}
