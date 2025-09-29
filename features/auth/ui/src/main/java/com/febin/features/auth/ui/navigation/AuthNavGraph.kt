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
    onAuthComplete: () -> Unit // Callback to navigate after successful auth
) {
    navigation(
        startDestination = AuthNavRoutes.Signin.route,
        route = NavRoutes.Auth.route // This is the route for the nested graph
    ) {
        composable(AuthNavRoutes.Signin.route) {
            SigninScreen(
                onSigninClicked = {
                    email, password ->
                    // TODO: Implement actual sign-in logic
                    // For now, directly call onAuthComplete as a placeholder
                    onAuthComplete()
                },
                onNavigateToSignup = {
                    navController.navigate(AuthNavRoutes.Signup.route) {
                        // Optional: popUpTo(AuthNavRoutes.Signin.route) { inclusive = true } if you want to clear signin from backstack
                    }
                }
            )
        }
        composable(AuthNavRoutes.Signup.route) {
            SignupScreen{
                navController.navigate(AuthNavRoutes.Signin.route)
                // Optional: popUpTo(AuthNavRoutes.Signup.route) { inclusive = true }
            }
        }
    }
}
