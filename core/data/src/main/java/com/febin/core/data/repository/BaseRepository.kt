package com.febin.core.data.repository

import com.febin.core.data.local.AppPreferences
import com.febin.core.domain.auth.TokenRefresher
import com.febin.core.domain.utils.DomainFailureException
import com.febin.core.domain.utils.Failure
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import timber.log.Timber
import java.io.IOException

/**
 * Base repository providing common API call execution logic, including refresh-and-retry for authenticated calls.
 */
abstract class BaseRepository(
    protected val appPreferences: AppPreferences, // MODIFIED: Changed to protected val
    private val tokenRefresher: TokenRefresher
) {

    // REMOVED: Protected getter for appPreferences is no longer needed

    protected suspend fun <DTO, DOMAIN> executeAuthenticatedApiCall(
        apiCall: suspend () -> DTO,
        onSuccessMapper: (DTO) -> DOMAIN
    ): Result<DOMAIN> {
        return try {
            val dto = apiCall()
            Result.success(onSuccessMapper(dto))
        } catch (e: ClientRequestException) {
            if (e.response.status.value == 401) {
                Timber.d("BaseRepository: Caught 401, attempting token refresh.")
                return try {
                    tokenRefresher.refreshToken()
                    Timber.d("BaseRepository: Token refresh successful, retrying original request.")
                    val retriedDto = apiCall()
                    Result.success(onSuccessMapper(retriedDto))
                } catch (refreshException: Exception) {
                    Timber.e(refreshException, "BaseRepository: Token refresh failed.")
                    // MODIFIED: Use direct protected access
                    appPreferences.clearUserInfo() 
                    Result.failure(DomainFailureException(Failure.AuthError("Your session has expired. Please sign in again.")))
                }
            } else {
                Timber.e(e, "BaseRepository: Client request failed (non-401). Code: ${e.response.status.value}")
                val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
                Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Client error: ${e.response.status.description}")))
            }
        }
        catch (e: ServerResponseException) {
            Timber.e(e, "BaseRepository: Server response error. Code: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Server error: ${e.response.status.description}")))
        }
        catch (e: IOException) {
            Timber.e(e, "BaseRepository: Network error.")
            Result.failure(DomainFailureException(Failure.NetworkError("Please check your internet connection.")))
        }
        catch (e: Exception) {
            Timber.e(e, "BaseRepository: Generic error: ${e.message}")
            Result.failure(DomainFailureException(Failure.GenericError(e.message ?: "An unexpected error occurred. Please try again.")))
        }
    }

    protected suspend fun <DTO, DOMAIN> executeApiCall(
        apiCall: suspend () -> DTO,
        onSuccessMapper: (DTO) -> DOMAIN
    ): Result<DOMAIN> {
        return try {
            val dto = apiCall()
            Result.success(onSuccessMapper(dto))
        } catch (e: ClientRequestException) {
            Timber.e(e, "BaseRepository (non-auth): Client request failed. Code: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Client error: ${e.response.status.description}")))
        }
        catch (e: ServerResponseException) {
            Timber.e(e, "BaseRepository (non-auth): Server response error. Code: ${e.response.status.value}")
            val errorBody = try { e.response.bodyAsText() } catch (ex: Exception) { null }
            Result.failure(DomainFailureException(Failure.HttpError(e.response.status.value, errorBody, "Server error: ${e.response.status.description}")))
        }
        catch (e: IOException) {
            Timber.e(e, "BaseRepository (non-auth): Network error.")
            Result.failure(DomainFailureException(Failure.NetworkError("Please check your internet connection.")))
        }
        catch (e: Exception) {
            Timber.e(e, "BaseRepository (non-auth): Generic error: ${e.message}")
            Result.failure(DomainFailureException(Failure.GenericError(e.message ?: "An unexpected error occurred. Please try again.")))
        }
    }
}
