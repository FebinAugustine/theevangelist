package com.febin.core.data.network

import com.febin.core.data.constants.Constants
import com.febin.core.data.dto.RefreshResponseDto
import com.febin.core.data.local.AppPreferences
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import kotlinx.serialization.Serializable
import timber.log.Timber



internal fun HttpClientConfig<*>.installAuth(
    appPreferences: AppPreferences
) {
    install(Auth) {
        bearer {
            loadTokens {
                loadAuthTokens(appPreferences)
            }
            refreshTokens { // 'this.client' is the HttpClient being configured
                refreshAuthTokens(this.client, appPreferences)
            }
            sendWithoutRequest { request ->
                val path = request.url.encodedPath
                path.endsWith("/login") || path.endsWith("/register") || path.endsWith("/refresh-token")
            }
        }
    }
}

private fun loadAuthTokens(appPreferences: AppPreferences): BearerTokens? {
    val accessToken: String = appPreferences.getAccessToken() ?: return null
    val refreshToken = appPreferences.getRefreshToken()
    return if (accessToken.isNotEmpty() && refreshToken?.isNotEmpty() == true) {
        BearerTokens(accessToken, refreshToken)
    } else {
        null
    }
}

private suspend fun refreshAuthTokens(
    client: HttpClient,
    appPreferences: AppPreferences
): BearerTokens? {
    val currentRefreshToken = appPreferences.getRefreshToken()
    if (currentRefreshToken?.isEmpty() != false) { // Handles null or empty
        Timber.w("Refresh token is null or empty. Cannot refresh.")
        return null
    }

    return try {
        Timber.d("Attempting to refresh token.")
        val response = client.post("${Constants.BASE_URL}/refresh-token") {
            header(HttpHeaders.Authorization, "Bearer $currentRefreshToken")
        }
        val newTokens = response.body<RefreshResponseDto>()
        Timber.d("Token refreshed successfully.")
        // Persist the new access token.
        // If the response includes a new refresh token, use it; otherwise, keep the old one.
        val finalRefreshToken = newTokens.refreshToken ?: currentRefreshToken
        appPreferences.saveTokens(newTokens.accessToken, finalRefreshToken)
        BearerTokens(newTokens.accessToken, finalRefreshToken)
    } catch (e: Exception) {
        Timber.e(e, "Token refresh failed.")
        // Optionally, clear tokens or handle specific errors (e.g., invalid refresh token)
        // appPreferences.clearAuthTokens() // Consider the implications of this
        null
    }
}
