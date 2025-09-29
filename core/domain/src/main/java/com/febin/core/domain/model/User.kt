package com.febin.core.domain.model

data class User(
    val id: String,
    val fullname: String,
    val email: String,
    val password: String,
    val phone: Long? = null,
    val fellowship: String? = null,
    val role: String? = null
)