package com.febin.core.domain.auth

/**
 * Interface defining the contract for refreshing authentication tokens.
 * This allows BaseRepository to be decoupled from specific feature implementations of token refresh.
 */
interface TokenRefresher {
    /**
     * Attempts to refresh the authentication tokens.
     * @throws Exception if the refresh fails (e.g., refresh token invalid, network error).
     *                   Success is indicated by no exception, implying new HttpOnly cookies are set.
     */
    suspend fun refreshToken()
}
