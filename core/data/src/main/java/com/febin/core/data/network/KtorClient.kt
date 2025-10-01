package com.febin.core.data.network

import com.febin.core.data.constants.Constants
import com.febin.core.data.dto.RefreshResponseDto
import com.febin.core.data.local.AppPreferences
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import kotlin.math.pow

fun createHttpClient(appPreferences: AppPreferences) = HttpClient(Android) {
    expectSuccess = false  // Don't auto-throw on non-2xx; handle in service

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
        level = LogLevel.ALL  // NONE in prod
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 10_000
        socketTimeoutMillis = 30_000
    }

    install(HttpRequestRetry) {
        // Combine response-based retry logic into a single predicate with the correct parameters.
        retryIf { request, response ->
            val code = response.status.value
            code in 500..599 || code == 408
        }

        // Use retryOnExceptionIf which expects (request, cause) -> Boolean
        retryOnExceptionIf { request, cause ->
            cause is ConnectException || cause is IOException
        }

        maxRetries = 3

        // delayMillis expects a function (attempt: Int) -> Long
        // Use attempt.toDouble() when calling pow
        delayMillis { attempt: Int ->
            val baseDelay = 1000.0 // ms
            val maxDelay = 10_000L
            val multiplier = 2.0
            // attempt can be 0.. so pow(attempt.toDouble())
            val computed = (baseDelay * multiplier.pow(attempt.toDouble())).toLong()
            computed.coerceAtMost(maxDelay)
        }
    }


    install(Auth) {
        bearer {
            loadTokens {
                loadAuthTokens(appPreferences)
            }
            refreshTokens {
                refreshAuthTokens(client, appPreferences)
            }
            sendWithoutRequest { request ->
                val path = request.url.encodedPath
                path.endsWith("/login") || path.endsWith("/register") || path.endsWith("/refresh-token")
            }
        }
    }

    install(HttpCallValidator) {
        handleResponseExceptionWithRequest { cause: Throwable, request: HttpRequest ->  // Fixed: Params are Throwable, HttpRequest
            if (cause is ClientRequestException) {
                val errorBody = cause.response.bodyAsText()  // Fixed: Use cause.response (not param)
                Timber.e("API Error (${cause.response.status.value}): $errorBody")
                throw Exception(errorBody)  // Re-throw with body
            }
        }
    }

    defaultRequest {
        url(Constants.BASE_URL)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}

private fun loadAuthTokens(appPreferences: AppPreferences): BearerTokens? {
    val accessToken = appPreferences.getToken()
    val refreshToken = appPreferences.getRefreshToken()
    return if (accessToken != null && refreshToken != null) {
        BearerTokens(accessToken, refreshToken)
    } else null
}

private suspend fun refreshAuthTokens(client: HttpClient, appPreferences: AppPreferences): BearerTokens? {
    val refreshToken = appPreferences.getRefreshToken()
    if (refreshToken == null) return null

    return try {
        val response = client.post("${Constants.BASE_URL}/refresh-token") {
            header(HttpHeaders.Authorization, "Bearer $refreshToken")
        }
        val newTokens = response.body<RefreshResponseDto>()
        appPreferences.saveTokens(newTokens.accessToken, newTokens.refreshToken)
        BearerTokens(newTokens.accessToken, newTokens.refreshToken)
    } catch (e: Exception) {
        Timber.e(e, "Token refresh failed")
        null
    }
}