package com.febin.features.auth.ui.viewmodel.validation

import android.util.Patterns

object ValidationUtils {
    fun validateFullname(fullname: String?): String? {
        val value = fullname?.trim().orEmpty()
        if (value.isEmpty()) return "Full name is required."
        if (value.length < 2) return "Full name is too short."
        if (value.length > 100) return "Full name is too long."
        return null
    }

    fun validateEmail(email: String?): String? {
        val value = email?.trim().orEmpty()
        if (value.isEmpty()) return "Email is required."
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) return "Invalid email format."
        return null
    }

    fun validatePassword(password: String?): String? {
        val value = password.orEmpty()
        if (value.isEmpty()) return "Password is required."
        if (value.length < 8) return "Password must be at least 8 characters."
        if (value.length > 128) return "Password cannot exceed 128 characters."
        return null
    }

    fun validatePhone(phone: String?): String? {
        val value = phone?.trim().orEmpty()
        if (value.isEmpty()) return "Phone is required."
        // basic digits-only check (adjust for international formatting if required)
        val digitsOnly = value.replace(Regex("[^\\d+]"), "")
        if (digitsOnly.length < 7) return "Phone number seems too short."
        if (digitsOnly.length > 15) return "Phone number seems too long."
        return null
    }

    fun validateFellowship(fellowship: String?): String? {
        val value = fellowship?.trim().orEmpty()
        if (value.isEmpty()) return "Fellowship is required."
        return null
    }

    fun validateRole(role: String?): String? {
        val value = role?.trim().orEmpty()
        if (value.isEmpty()) return "Role is required."
        // optionally validate allowed roles
        val allowed = setOf("USER", "ADMIN", "SUPER_ADMIN")
        if (value.isNotEmpty() && value !in allowed) return "Invalid role selected."
        return null
    }
}