package com.febin.features.auth.domain.useCases

import com.febin.features.auth.domain.repository.AuthRepository

class SignupUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(fullname: String, email: String, password: String, phone: String, fellowship: String, role: String) = authRepository.signup(fullname, email, password, phone, fellowship, role)
}