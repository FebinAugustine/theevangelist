package com.febin.features.auth.data.repository

import com.febin.core.data.local.AppPreferences
import com.febin.core.domain.model.User
import com.febin.features.auth.data.dto.SignupRequestDto
import com.febin.features.auth.data.remote.services.AuthApiService
import com.febin.features.auth.domain.repository.AuthRepository
import com.febin.features.auth.domain.utils.Failure
import timber.log.Timber

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

        return try {
            val requestDto = SignupRequestDto(fullname, email, password, phone, fellowship, role)
            val responseDto = authApiService.signup(requestDto) // Gets the new SignupResponseDto

            // If Ktor's expectSuccess = true, a non-2xx status would have thrown an exception.
            // So, reaching here means HTTP success.
            Timber.i("Signup successful via API: ${responseDto.message}")

            // Construct the domain User object from input and response
            val createdUser = User(
                id = responseDto.email, // Using email as ID, as backend doesn't return one.
                // This needs to align with your app's ID strategy.
                fullname = fullname,// From input
                email = responseDto.email, // From response (should match input)
                password = password,    // From input
                phone = phone.toLongOrNull(), // Convert String to Long?, handle parsing error if necessary
                fellowship = fellowship, // From input
                role = responseDto.appUserRole ?: role // Prefer backend role, fallback to form input role
            )

            // Here you might want to save tokens if your AppPreferences is designed for that
            // e.g., if signup also logs the user in and returns tokens (though this response doesn't show tokens)
            // appPreferences.saveAuthInfo(...)

            Result.success(createdUser)

        } catch (e: Exception) {
            Timber.e(e, "Signup failed in repository")
            Result.failure(Failure.fromException(e))
        }
    }
}