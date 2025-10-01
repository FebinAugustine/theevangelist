package com.febin.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    val fullName: String,
    val fellowship: String,
    val phoneNo: String,  // String for flexibility (parse to Long if needed)
    val email: String,
    val password: String,
    val appUserRole: String
)