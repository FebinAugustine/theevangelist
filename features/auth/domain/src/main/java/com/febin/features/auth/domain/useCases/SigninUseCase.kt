package com.febin.features.auth.domain.useCases

import com.febin.features.auth.domain.repository.AuthRepository

class SigninUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke( email: String, password: String) = authRepository.signin( email, password)
}