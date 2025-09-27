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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject



@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val prefsManager = koinInject<SharedPreferencesManager>()
    val coroutineScope = rememberCoroutineScope()
    var currentRole by remember { mutableStateOf<String?>(null) }

    // Load role after delay (simulate async from auth/prefs)
    LaunchedEffect(Unit) {
        delay(500)
        currentRole = prefsManager.getCurrentRole()  // Assume method in manager
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen{
                if (prefsManager.isOnboardingCompleted()){
                    if (currentRole != null) {
                        when (currentRole) {
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
                coroutineScope.launch {
                    currentRole = prefsManager.getCurrentRole()  // Reload role
                    if (currentRole != null) {
                        when (currentRole) {
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
        }
    }
}