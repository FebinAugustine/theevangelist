package com.febin.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val fullname: String,
    val email: String,
    val phone: Long? = null,
    val fellowship: String? = null,
    val role: String? = null,
    val message: String? = null  // Optional success/error message from response
)