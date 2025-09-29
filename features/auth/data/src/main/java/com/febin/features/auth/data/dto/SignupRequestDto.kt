package com.febin.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    @SerialName("fullName") // Tells serializer to use "fullName" in JSON
    val fullname: String,

    val email: String, // Assuming "email" is correct

    val password: String, // Assuming "password" is correct

    @SerialName("phoneNo") // Tells serializer to use "phoneNo" in JSON
    val phone: String,

    val fellowship: String, // Assuming "fellowship" is correct

    @SerialName("appUserRole") // Tells serializer to use "appUserRole" in JSON
    val role: String
)