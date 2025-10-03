package com.febin.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model for User (from signup/signin/getCurrentUser).
 * - Immutable; used in UseCases and MVI state.
 */
@Serializable
data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val fellowship: String? = null,
    val role: String = "USER"
)