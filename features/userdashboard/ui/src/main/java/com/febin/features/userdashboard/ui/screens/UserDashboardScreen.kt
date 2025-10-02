package com.febin.features.userdashboard.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // Changed import if it was NavController.Companion
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.febin.features.userdashboard.ui.screens.screens.ReportsScreen
import com.febin.features.userdashboard.ui.screens.screens.SettingsScreen

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Reports : BottomNavItem("reports", "Reports", Icons.Default.Book)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    mainNavController: NavController // Corrected: accept NavController instance
) {

    val internalNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Reports,
        BottomNavItem.Profile,
        BottomNavItem.Settings,
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5), // Light Gray background
        bottomBar = {
            NavigationBar(
                containerColor = Color.White, // White background for the bottom bar
                modifier = Modifier.shadow(elevation = 10.dp)
            ) {
                val navBackStackEntry by internalNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            internalNavController.navigate(screen.route) {
                                popUpTo(internalNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        NavHost(
            navController = internalNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
//                HomeScreen()
            }
            composable(BottomNavItem.Reports.route) {
                ReportsScreen()
            }
            composable(BottomNavItem.Profile.route) {
//                ProfileScreen(navController = mainNavController) // This now correctly passes the instance
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
        }
    }
}