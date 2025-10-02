package com.febin.core.domain.utils

/**
 * Sealed Failure for domain errors (MVI-friendly).
 * - Maps from exceptions or API responses.
 */
sealed class Failure {
    object NetworkError : Failure()
    object ServerError : Failure()
    data class AuthError(val message: String) : Failure()
    data class Unknown(val t: Throwable) : Failure()

    /**
     * Human-friendly message suitable for displaying to users / logs.
     * Renamed to avoid JVM signature collision with `getMessage()` generated for properties.
     */
    fun Failure.userMessage(): String = when (this) {
        is Failure.NetworkError -> "Network error. Please check your connection."
        is Failure.ServerError -> "Server error. Please try again."
        is Failure.AuthError -> this.message
        is Failure.Unknown -> this.t.message ?: "An unexpected error occurred."
    }

    companion object {
        fun fromException(e: Exception): Failure = when {
            e.message?.contains("network", ignoreCase = true) == true -> NetworkError
            e.message?.contains("401") == true -> AuthError("Unauthorized")
            e.message?.contains("500") == true -> ServerError
            else -> Unknown(e)
        }
    }
}
