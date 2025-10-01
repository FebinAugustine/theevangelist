package com.febin.features.userdashboard.data.mapper

import com.febin.core.domain.model.User
import com.febin.features.userdashboard.data.dto.GetUserResponseDto

/**
 * Mappers for User DTOs ↔ Domain User.
 */
object UserMapper {

    fun toDomain(getUserDto: GetUserResponseDto): User = User(
        id = "",  // From prefs
        fullname = getUserDto.fullName,
        email = getUserDto.email,
        phone = getUserDto.phoneNo,
        fellowship = getUserDto.fellowship,
        role = getUserDto.role
    )
}