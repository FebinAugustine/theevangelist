package com.febin.features.auth.domain.useCases

import com.febin.core.domain.model.User
import com.febin.core.domain.utils.Failure
import com.febin.core.domain.utils.Result

import com.febin.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignupUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Invokes the signup flow.
     * @return Flow<Result<User>>.
     */
    operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        phone: String,
        fellowship: String,
        role: String

    ): Flow<Result<User>> = flow {
        emit(Result.loading())

        // The repository currently exposes a Flow<Result<User>>.
        // Collect its emissions and forward them to collectors of this use-case.
        val repoFlow = authRepository.signup(fullName, email, password, phone, fellowship, role)
        repoFlow.collect { repoEmission ->
            emit(repoEmission)
        }

    }


}