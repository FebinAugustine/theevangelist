package com.febin.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SigninRequestDto(
    val email: String,
    val password: String
)