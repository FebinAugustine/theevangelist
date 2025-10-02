package com.febin.features.auth.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.febin.core.ui.navigation.NavRoutes // Main app navigation routes
import com.febin.features.auth.ui.screens.SigninScreen
import com.febin.features.auth.ui.screens.SignupScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onNavigateToUserDashboard: (userId: String) -> Unit,
    onNavigateToAdminDashboard: (userId: String) -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    navigation(
        startDestination = AuthNavRoutes.Signin.route,
        route = NavRoutes.Auth.route // This is the route for the nested graph
    ) {
        composable(AuthNavRoutes.Signin.route) {
            SigninScreen(
                // ViewModel is koinInjectable within SigninScreen
                onNavigateToUserDashboard = onNavigateToUserDashboard,
                onNavigateToAdminDashboard = onNavigateToAdminDashboard,
                onNavigateToSignup = {
                    navController.navigate(AuthNavRoutes.Signup.route)
                },
                onNavigateToForgotPassword = onNavigateToForgotPassword // Pass down forgot password callback
            )
        }
        composable(AuthNavRoutes.Signup.route) {
            SignupScreen(
                // ViewModel is koinInjectable within SignupScreen
                onNavigateToLogin = { 
                    navController.navigate(AuthNavRoutes.Signin.route) {
                        // Pop up to Signin, ensuring Signup is removed from backstack,
                        // and if user is already at Signin (e.g. deep link or re-navigation),
                        // it effectively re-selects it without adding to stack.
                        popUpTo(AuthNavRoutes.Signin.route) { inclusive = true }
                    }
                }
            )
        }
        // TODO: Add composable for Forgot Password if it's part of this auth graph
        // composable(AuthNavRoutes.ForgotPassword.route) { /* ForgotPasswordScreen(...) */ }
    }
}
