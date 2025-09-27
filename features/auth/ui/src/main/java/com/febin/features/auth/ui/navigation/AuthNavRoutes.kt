package com.febin.features.auth.ui.navigation

internal sealed class AuthNavRoutes(val route: String) {
    object Signin : AuthNavRoutes("signin")
    object Signup : AuthNavRoutes("signup")
}
