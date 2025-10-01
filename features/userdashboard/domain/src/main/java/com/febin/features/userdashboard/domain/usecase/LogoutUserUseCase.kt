package com.febin.features.userdashboard.domain.usecase

import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.features.userdashboard.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * UseCase for logout.
 * - Calls repo → Flow<Result<Unit>>.
 */
class LogoutUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Result<Unit>> = flow {
        emit(Result.loading())

        try {
            repository.logout().collect { repoResult ->
                emit(repoResult)
            }
        } catch (e: Exception) {
            emit(Result.failure(Failure.fromException(e)))
        }
    }
}
