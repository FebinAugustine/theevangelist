package com.febin.features.auth.data.mapper

import com.febin.core.domain.model.User
import com.febin.features.auth.data.dto.SigninResponseDto
import com.febin.features.auth.data.dto.SignupResponseDto

/**
 * Mappers for Auth DTOs ↔ Domain User.
 */
object AuthMapper {

    fun toDomain(signinDto: SigninResponseDto): User = User(
        id = "",  // From prefs or API if added
        fullname = signinDto.fullname,  // Available in signin response
        email = signinDto.email,
        phone = signinDto.phone,
        fellowship = signinDto.fellowship,
        role = signinDto.appUserRole
    )

    fun toDomain(signupDto: SignupResponseDto): User = User(
        id = "",  // From prefs (fetch after signup)
        fullname = "",  // Not in response—fetch from getCurrentUser
        email = signupDto.email,
        phone = null,  // Not in response
        fellowship = null,  // Not in response
        role = signupDto.appUserRole
    )
}