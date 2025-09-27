package com.febin.core.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.febin.core.ui.navigation.NavRoutes


/**
 * Reusable SharedNavHost for common navigation logic.
 * - Composable; used in MainActivity for root.
 * - Can add deep links or shared args here.
 */
@Composable
fun SharedNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.Splash.route,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Common composable (e.g., error screen)
        composable("error") { /* ErrorScreen */ }
        // Feature graphs
        builder()
    }
}