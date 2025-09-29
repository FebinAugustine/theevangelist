package com.febin.core.data.network

import com.febin.core.data.constants.Constants
// AppPreferences import removed as it's no longer a parameter
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
// import io.ktor.client.plugins.ClientRequestException // No longer explicitly needed here
// import io.ktor.client.plugins.HttpCallValidator // REMOVING
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
// import io.ktor.client.statement.bodyAsText // Not directly used here anymore
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import kotlin.math.pow


/**
 * Creates a Ktor HttpClient for Android, relying on HttpOnly cookies for auth.
 * - Uses Android engine for native cookie handling.
 * - JSON serialization, Timber logging, error validation.
 */
// AppPreferences parameter removed
fun createHttpClient() = HttpClient(Android) {
    expectSuccess = true // This will throw HttpResponseException for non-2xx responses

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Timber.d("Ktor => %s", message)
            }
        }
        level = LogLevel.ALL  // Change to LogLevel.NONE in prod
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 10_000
        socketTimeoutMillis = 30_000
    }

    install(HttpRequestRetry) {
        retryIf { _, response -> response.status.value in 500..599 }
        retryIf { _, response -> response.status.value == 408 }
        retryOnExceptionIf { _, cause ->
            cause is ConnectException || cause is IOException
        }
        maxRetries = 3
        delayMillis { retryAttempt ->
            val initialDelayMs = 1000L
            val maximumDelayMs = 10000L
            val backoffFactor = 2.0
            val currentDelay = initialDelayMs * backoffFactor.pow(retryAttempt - 1)
            currentDelay.toLong().coerceAtMost(maximumDelayMs)
        }
    }

    // The Auth plugin configured for Bearer tokens is removed.
    // Cookie management for HttpOnly cookies will be handled by the Ktor Android engine.
    // installAuth(appPreferences) 

    defaultRequest {
        url(Constants.BASE_URL)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(HttpHeaders.UserAgent, "TheEvangelistApp/1.0")
    }
}
