package com.febin.features.auth.ui.utils

import android.util.Patterns

class SignupValidator {
    fun validateFullName(fullName: String): String? {
        if (fullName.isBlank()) return "Full name is required."
        if (fullName.length < 3) return "Full name must be at least 3 characters."
        if (fullName.length > 30) return "Full name cannot exceed 30 characters."
        return null
    }

    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email is required."
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email format."
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password is required."
        if (password.length < 7) return "Password must be at least 7 characters."
        if (password.length > 25) return "Password cannot exceed 25 characters."
        if (!password.any { it.isUpperCase() }) return "Password must contain at least one uppercase letter."
        if (!password.any { it.isDigit() }) return "Password must contain at least one digit."
        if (!password.any { it.isLowerCase() }) return "Password must contain at least one lowercase letter."
        if (!password.any { !it.isLetterOrDigit() }) return "Password must contain at least one special character."
        return null
    }
    fun validatePhone(phone: String): String? {
        if (phone.isBlank()) return "Phone number is required."
        if (!Patterns.PHONE.matcher(phone).matches()) return "Invalid phone number format."
        if (phone.length < 10) return "Phone number must be at least 10 digits."
        if (phone.length > 15) return "Phone number cannot exceed 15 digits."
        return null
    }
    fun validateFellowship(fellowship: String): String? {
        if (fellowship.isBlank()) return "Fellowship is required."
        return null
    }
    fun validateRole(role: String): String? {
        if (role.isBlank()) return "Role is required."
        return null
    }


}