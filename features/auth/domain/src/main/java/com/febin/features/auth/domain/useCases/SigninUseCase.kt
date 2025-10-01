package com.febin.features.auth.domain.useCases

import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result
import com.febin.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * UseCase for signin.
 * - Validates input → Calls repo → Returns Flow<Result<User>> for MVI.
 * - Handles basic UI/business validation; emits loading/success/error.
 */
class SigninUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Result<User>> = flow {
        emit(Result.loading())

        try {
            val repoFlow = repository.signin(email, password)
            repoFlow.collect {repoEmission ->
                emit(repoEmission)
            }
        } catch (t: Throwable) {
            emit(Result.isFailure(t))
        }
    }
}