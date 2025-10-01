package com.febin.features.auth.ui.utils

import android.util.Patterns

class SigninValidator() {


    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email is required."
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email format."
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password is required."
        if (password.length < 7) return "Password must be at least 7 characters."
        if (password.length > 25) return "Password cannot exceed 25 characters."
        return null
    }


}