package com.febin.features.auth.data.repository

import com.febin.core.data.local.AppPreferences
import com.febin.core.domain.model.User
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.remote.services.AuthApiService
import com.febin.features.auth.domain.repository.AuthRepository
import com.febin.features.auth.domain.utils.DomainFailureException // ADDED
import com.febin.features.auth.domain.utils.Failure
import io.ktor.client.plugins.ClientRequestException // ADDED
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import timber.log.Timber
import java.io.IOException

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val appPreferences: AppPreferences
) : AuthRepository {

    override suspend fun signup(
        fullname: String,
        email: String,
        password: String,
        phone: String,
        fellowship: String,
        role: String // This is the role from the signup form
    ): Result<User> {

        // Removed stray HttpResponseReceiveFailed from here

        return try {
            val requestDto = SignupRequestDto(fullname, email, password, phone, fellowship, role)
            val responseDto = authApiService.signup(requestDto)

            Timber.i("Signup successful via API: ${responseDto.message}")

            val createdUser = User(
                id = responseDto.email,
                fullname = fullname,
                email = responseDto.email,
                password = password,
                phone = phone.toLongOrNull(),
                fellowship = fellowship,
                role = responseDto.appUserRole ?: role
            )
            Result.success(createdUser)

        } catch (e: Exception) {
            Timber.e(e, "Signup failed in repository")
            val domainFailure: Failure = when (e) {
                is IOException -> Failure.NetworkError("Please check your internet connection.")
                is ClientRequestException -> {
                    val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { 
                        Timber.e(ex, "Failed to read error body from ClientRequestException")
                        null 
                    }
                    Failure.HttpError(e.response.status.value, errorBody, "Client error: ${e.response.status.value}")
                }

                is ServerResponseException -> {
                    val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { 
                        Timber.e(ex, "Failed to read error body from HttpResponseReceiveFailed")
                        null 
                    }
                    Failure.HttpError(e.response.status.value, errorBody, "Failed to receive full response: ${e.response.status.value}")
                }
                else -> Failure.GenericError(e.message ?: "An unexpected error occurred. Please try again.")
            }
            Result.failure(DomainFailureException(domainFailure)) // MODIFIED: Wrapped in DomainFailureException
        }
    }
}