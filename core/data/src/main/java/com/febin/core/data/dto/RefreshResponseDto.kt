package com.febin.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshResponseDto(
    val accessToken: String,
    val refreshToken: String? = null // refreshToken can be optional in the response
)