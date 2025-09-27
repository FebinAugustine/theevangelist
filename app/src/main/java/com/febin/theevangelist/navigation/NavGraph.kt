package com.febin.theevangelist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.febin.core.ui.navigation.NavRoutes
import com.febin.core.ui.screens.OnboardingScreen
import com.febin.core.ui.screens.SplashScreen
import com.febin.di.data.SharedPreferencesManager
import com.febin.features.auth.ui.navigation.authNavGraph // Added import
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject



@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val prefsManager = koinInject<SharedPreferencesManager>()
    val coroutineScope = rememberCoroutineScope()
    var currentRole by remember { mutableStateOf<String?>(null) }

    // Load role initially. This helps decide the first screen after splash/onboarding.
    LaunchedEffect(Unit) {
        // Simulate a delay for loading role if necessary, e.g., from async storage
        delay(100) // Reduced delay as auth flow will also update it.
        currentRole = prefsManager.getCurrentRole()
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen {
                if (prefsManager.isOnboardingCompleted()) {
                    // Role might be loaded by now from LaunchedEffect
                    val role = prefsManager.getCurrentRole() // Re-check role here
                    if (role != null) {
                        when (role) {
                            "SUPER_ADMIN" -> navController.navigate(NavRoutes.Sadb.route) {
                                popUpTo(NavRoutes.Splash.route) { inclusive = true }
                            }
                            else -> navController.navigate(NavRoutes.UserDashboard.route) {
                                popUpTo(NavRoutes.Splash.route) { inclusive = true }
                            }
                        }
                    } else {
                        navController.navigate(NavRoutes.Auth.route) {
                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        }
                    }
                } else {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen {
                prefsManager.setOnboardingCompleted(true)
                // After onboarding, check role to navigate to Auth or Dashboard
                val role = prefsManager.getCurrentRole()
                if (role != null) {
                    when (role) {
                        "SUPER_ADMIN" -> navController.navigate(NavRoutes.Sadb.route) {
                            popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                        }
                        else -> navController.navigate(NavRoutes.UserDashboard.route) {
                            popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                        }
                    }
                } else {
                    navController.navigate(NavRoutes.Auth.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            }
        }

        // Integrate AuthNavGraph
        authNavGraph(navController = navController) {
            // This onAuthComplete callback is triggered from your Signin/Signup screens
            // after the (placeholder) auth logic.
            coroutineScope.launch {
                val role = prefsManager.getCurrentRole() // Re-fetch role after auth attempt
                currentRole = role // Update the state
                if (role != null) {
                    when (role) {
                        "SUPER_ADMIN" -> navController.navigate(NavRoutes.Sadb.route) {
                            popUpTo(NavRoutes.Auth.route) { inclusive = true } // Clear Auth flow
                        }
                        else -> navController.navigate(NavRoutes.UserDashboard.route) {
                            popUpTo(NavRoutes.Auth.route) { inclusive = true } // Clear Auth flow
                        }
                    }
                } else {
                    // Fallback: If role is still null after auth (e.g., placeholder or failed login)
                    // Navigate back to sign-in or splash. For simplicity, going to Splash.
                    // In a real app, you might show an error on the SigninScreen itself.
                    navController.navigate(NavRoutes.Splash.route) { // Or AuthNavRoutes.Signin.route if preferred
                        popUpTo(NavRoutes.Auth.route) { inclusive = true }
                    }
                }
            }
        }

        // TODO: Add other main navigation destinations like UserDashboard and Sadb
        // Example:
        // composable(NavRoutes.UserDashboard.route) { /* UserDashboardScreen() */ }
        // composable(NavRoutes.Sadb.route) { /* SuperAdminDashboardScreen() */ }
    }
}
