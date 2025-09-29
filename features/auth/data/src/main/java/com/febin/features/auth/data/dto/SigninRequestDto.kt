package com.febin.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SigninRequestDto(
    val email: String, // Assuming "email" is correct
    val password: String, // Assuming "password" is correct
)