package com.febin.features.userdashboard.domain.usecase

import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.features.userdashboard.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * UseCase for getting current user (post-signin).
 * - Calls repo → Flow<Result<User>>.
 */
class GetCurrentUserUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Result<User>> = flow {
        emit(Result.loading())

        try {
            repository.getCurrentUser().collect { repoResult ->
                emit(repoResult)
            }
        } catch (e: Exception) {
            emit(Result.failure(Failure.fromException(e)))
        }
    }


}