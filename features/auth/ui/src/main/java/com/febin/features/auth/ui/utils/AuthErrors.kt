package com.febin.features.auth.ui.utils

import com.febin.core.domain.utils.Failure as CoreFailure

/**
 * Single unified set of user-facing auth errors for Login / Signup flows.
 * Each variant provides a userMessage() to show in the UI.
 */
sealed class AuthError(open val message: String? = null) {
    // Shared
    object NetworkUnavailable : AuthError("No internet connection.")
    data class Unexpected(override val message: String) : AuthError(message)

    // Signup-specific
    object EmailAlreadyExists : AuthError("Email already exists.")
    object InvalidPhone : AuthError("Invalid phone number.")
    data class ValidationError(override val message: String) : AuthError(message)

    fun userMessage(): String = message ?: "An unexpected error occurred."
}

/**
 * Map core/domain Failure to an AuthError. Keep this centralized.
 */
fun mapCoreFailureToAuthError(core: CoreFailure): AuthError {
    return when (core) {
        is CoreFailure.NetworkError -> AuthError.NetworkUnavailable
        is CoreFailure.AuthError -> AuthError.ValidationError(core.message)
        is CoreFailure.ServerError -> AuthError.Unexpected("Server error. Please try again.")
        is CoreFailure.Unknown -> AuthError.Unexpected(core.t.message ?: "An unknown error occurred.")
        else -> AuthError.Unexpected("An unexpected error occurred.")
    }
}
