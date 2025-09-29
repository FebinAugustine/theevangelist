package com.febin.features.auth.domain.utils

import java.io.IOException

sealed class Failure(message: String) : IOException(message) {
    class NetworkFailure(message: String = "No internet connection. Please try again.") : Failure(message)
    class ServerFailure(message: String = "Our servers are having a problem. Please try again later.") : Failure(message)
    class UnknownFailure(message: String = "An unknown error occurred. Please try again.") : Failure(message)

    companion object {
        fun fromException(e: Exception): Failure {
            return when (e) {
                is IOException -> NetworkFailure()
                else -> UnknownFailure(e.message ?: "An unknown error occurred")
            }
        }
    }
}