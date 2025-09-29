package com.febin.features.auth.data.repository

import com.febin.core.data.local.AppPreferences
import com.febin.core.data.repository.BaseRepository
import com.febin.core.domain.model.User
import com.febin.core.domain.auth.TokenRefresher 
import com.febin.core.domain.utils.DomainFailureException 
import com.febin.core.domain.utils.Failure 
import com.febin.features.auth.data.dto.SigninRequestDto
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.remote.services.AuthApiService
import com.febin.features.auth.domain.repository.AuthRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import timber.log.Timber
import java.io.IOException

class AuthRepositoryImpl(
    private val authApiService: AuthApiService, 
    appPreferences: AppPreferences 
) : BaseRepository(appPreferences, authApiService as TokenRefresher), AuthRepository { 

    override suspend fun signup(
        fullname: String,
        email: String,
        password: String,
        phone: String,
        fellowship: String,
        role: String
    ): Result<User> {
        return try {
            val requestDto = SignupRequestDto(fullname, email, password, phone, fellowship, role)
            val responseDto = authApiService.signup(requestDto)

            Timber.i("Signup successful via API: ${responseDto.message}")
            appPreferences.saveUserInfo(
                userId = responseDto.email,
                userType = responseDto.appUserRole ?: role
            )
            val createdUser = User(
                id = responseDto.email,
                fullname = fullname,
                email = responseDto.email,
                password = "", 
                phone = phone.toLongOrNull(),
                fellowship = fellowship,
                role = responseDto.appUserRole ?: role
            )
            Result.success(createdUser)
        } catch (e: ClientRequestException) {
            Timber.e(e, "Signup ClientRequestException: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Client error: ${e.response.status.description}")))
        } catch (e: ServerResponseException) {
            Timber.e(e, "Signup ServerResponseException: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Server error: ${e.response.status.description}")))
        } catch (e: IOException) {
            Timber.e(e, "Signup IOException")
            Result.failure(DomainFailureException(Failure.NetworkError("Please check your internet connection.")))
        } catch (e: Exception) {
            Timber.e(e, "Signup Generic Exception: ${e.message}")
            Result.failure(DomainFailureException(Failure.GenericError(e.message ?: "An unexpected error occurred during signup.")))
        }
    }

    override suspend fun signin(email: String, password: String): Result<User> {
        val requestDto = SigninRequestDto(email, password)
        // MODIFIED: Use direct try-catch for signin to correctly handle 401s as direct HttpErrors
        return try {
            val responseDto = authApiService.signin(requestDto)
            Timber.i("Signin successful via API: ${responseDto.message}")
            appPreferences.saveUserInfo(
                userId = responseDto.email,
                userType = responseDto.appUserRole
            )
            Result.success(User(
                id = responseDto.email,
                fullname = "", // Signin doesn't usually return fullname, can be fetched later
                email = responseDto.email,
                password = "", // Don't store password in domain model
                phone = null, // Signin doesn't usually return phone
                fellowship = null, // Signin doesn't usually return fellowship
                role = responseDto.appUserRole
            ))
        } catch (e: ClientRequestException) {
            Timber.e(e, "Signin ClientRequestException: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Client error: ${e.response.status.description}")))
        } catch (e: ServerResponseException) {
            Timber.e(e, "Signin ServerResponseException: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Server error: ${e.response.status.description}")))
        } catch (e: IOException) {
            Timber.e(e, "Signin IOException")
            Result.failure(DomainFailureException(Failure.NetworkError("Please check your internet connection.")))
        } catch (e: Exception) {
            Timber.e(e, "Signin Generic Exception: ${e.message}")
            Result.failure(DomainFailureException(Failure.GenericError(e.message ?: "An unexpected error occurred during signin.")))
        }
    }
}
