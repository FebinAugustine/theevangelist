package com.febin.core.domain.utils // MODIFIED PACKAGE

// Sealed class for representing different types of failures.
sealed class Failure {
    // Abstract property for a user-friendly message associated with the failure.
    abstract val message: String

    /**
     * Represents a network-related failure (e.g., no internet connection, DNS issues).
     * @param message A user-friendly message describing the network error.
     */
    data class NetworkError(override val message: String) : Failure()

    /**
     * Represents an error originating from an HTTP response (e.g., 4xx or 5xx status codes).
     * @param code The HTTP status code.
     * @param errorBody The raw error body string received from the server, if any.
     * @param message A generic user-friendly message for this type of HTTP error.
     *                More specific messages can be derived in the ViewModel based on the code and errorBody.
     */
    data class HttpError(
        val code: Int,
        val errorBody: String?,
        override val message: String
    ) : Failure()

    /**
     * Represents a generic or unexpected failure that doesn't fit into other categories.
     * @param message A user-friendly message describing the generic error.
     */
    data class GenericError(override val message: String) : Failure()

    /**
     * Represents an authentication-specific failure (e.g., refresh token invalid, session fully expired).
     * @param message A user-friendly message indicating the authentication problem.
     */
    data class AuthError(override val message: String) : Failure()
}

/**
 * A custom Exception class to wrap domain-specific Failure objects,
 * allowing them to be used with Kotlin's standard Result.failure().
 */
class DomainFailureException(val domainFailure: Failure) : Exception(domainFailure.message)
