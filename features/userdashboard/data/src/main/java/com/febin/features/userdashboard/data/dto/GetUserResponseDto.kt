package com.febin.features.userdashboard.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponseDto(
    val fullName: String,
    val email: String,
    val phoneNo: String? = null,
    val role: String,
    val fellowship: String? = null
)