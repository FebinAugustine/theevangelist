package com.febin.theevangelist.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.febin.core.ui.navigation.NavRoutes
import com.febin.core.ui.screens.OnboardingScreen
import com.febin.core.ui.screens.SplashScreen
import com.febin.di.data.SharedPreferencesManager // Assuming this wraps AppPreferences
import com.febin.features.auth.ui.navigation.AuthNavRoutes // For ForgotPassword placeholder
import com.febin.features.auth.ui.navigation.authNavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import timber.log.Timber


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val prefsManager = koinInject<SharedPreferencesManager>()
    val coroutineScope = rememberCoroutineScope() // Keep for potential future use
    var currentRole by remember { mutableStateOf<String?>(null) } // Keep for initial load

    LaunchedEffect(Unit) {
        delay(100) // Simulate initial loading if any
        currentRole = prefsManager.getCurrentRole() // Fetch initial role
        Timber.d("AppNavGraph: Initial role loaded: $currentRole")
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen {
                if (prefsManager.isOnboardingCompleted()) {
                    val role = prefsManager.getCurrentRole() // Re-check role
                    Timber.d("AppNavGraph - Splash: Onboarding complete. Role: $role")
                    if (role != null) {
                        when (role.uppercase()) { // Robust comparison
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
                    Timber.d("AppNavGraph - Splash: Navigating to Onboarding")
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen {
                prefsManager.setOnboardingCompleted(true)
                val role = prefsManager.getCurrentRole()
                Timber.d("AppNavGraph - Onboarding: Completed. Role: $role")
                if (role != null) {
                    when (role.uppercase()) {
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

        authNavGraph(
            navController = navController,
            onAuthComplete = { userId ->
                // userId is available from SigninScreen, can be used if dashboards need it directly.
                Timber.d("AppNavGraph - Auth Complete. UserID: $userId. Fetching role.")
                val role = prefsManager.getCurrentRole() // Role saved during sign-in by AuthRepositoryImpl
                currentRole = role // Update local state if needed
                Timber.d("AppNavGraph - Auth Complete. Role from Prefs: $role")
                if (role != null) {
                    when (role.uppercase()) {
                        "SUPER_ADMIN" -> navController.navigate(NavRoutes.Sadb.route) {
                            popUpTo(NavRoutes.Auth.route) { inclusive = true } // Clear Auth flow
                        }
                        else -> navController.navigate(NavRoutes.UserDashboard.route) {
                            popUpTo(NavRoutes.Auth.route) { inclusive = true } // Clear Auth flow
                        }
                    }
                } else {
                    // Fallback: Should ideally not happen if sign-in was successful and role was saved.
                    Timber.e("AppNavGraph - Auth Complete: Role is null after auth. Navigating to Splash.")
                    navController.navigate(NavRoutes.Splash.route) { 
                        popUpTo(NavRoutes.Auth.route) { inclusive = true }
                    }
                }
            },
            onNavigateToForgotPassword = {
                Timber.d("AppNavGraph: Navigate to Forgot Password triggered.")
                // Assuming ForgotPasswordScreen is part of authNavGraph and AuthNavRoutes.ForgotPassword exists
                // navController.navigate(AuthNavRoutes.ForgotPassword.route)
                // TODO: Define ForgotPasswordScreen and its route in AuthNavGraph if not already present.
                // For now, this might be a no-op if the route isn't defined in authNavGraph.
            }
        )

        composable(NavRoutes.UserDashboard.route) {
            // TODO: Replace with actual UserDashboardScreen(navController, userId (optional))
            Text("User Dashboard Screen") 
        }
        composable(NavRoutes.Sadb.route) {
            // TODO: Replace with actual SadbScreen(navController, userId (optional))
            Text("Super Admin (SADB) Dashboard Screen")
        }
    }
}
