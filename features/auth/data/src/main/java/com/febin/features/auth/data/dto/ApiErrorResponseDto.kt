package com.febin.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponseDto(
    val message: String? = null,
    val errors: Map<String, String>? = null,
    val error: String? = null,
)
