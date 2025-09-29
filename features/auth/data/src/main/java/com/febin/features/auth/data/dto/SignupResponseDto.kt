package com.febin.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponseDto(
    val email: String,
    val appUserRole: String? = null, // Matches your backend field
    val message: String? = null
)