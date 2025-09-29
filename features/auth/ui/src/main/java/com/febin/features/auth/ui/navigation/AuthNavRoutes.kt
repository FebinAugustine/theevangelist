package com.febin.features.auth.ui.navigation

// MODIFIED: Removed 'internal' to make it public
sealed class AuthNavRoutes(val route: String) {
    object Signin : AuthNavRoutes("signin")
    object Signup : AuthNavRoutes("signup")
    // object ForgotPassword : AuthNavRoutes("forgot_password") // Add when ForgotPasswordScreen is ready
}
