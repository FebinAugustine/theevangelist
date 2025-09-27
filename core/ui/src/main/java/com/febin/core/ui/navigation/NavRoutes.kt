package com.febin.core.ui.navigation

/**
 * Sealed class for global navigation routes.
 * - Shared across features; pure Kotlin.
 */
sealed class NavRoutes(val route: String) {
    // Core routes
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")

    // Feature routes (role-based)
    object Auth : NavRoutes("auth")
    object UserDashboard : NavRoutes("user_dashboard")
    object Sadb : NavRoutes("sadb")  // Super admin dashboard
}