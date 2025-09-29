package com.febin.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SigninResponseDto(
    val email: String, 
    val appUserRole: String?,
    val message: String? = null
)
