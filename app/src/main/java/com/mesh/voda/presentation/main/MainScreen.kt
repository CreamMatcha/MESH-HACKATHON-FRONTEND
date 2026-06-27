package com.mesh.voda.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mesh.voda.navigation.Screen
import com.mesh.voda.presentation.activity.ActivityScreen
import com.mesh.voda.presentation.home.HomeScreen
import com.mesh.voda.presentation.saved.SavedScreen
import com.mesh.voda.presentation.search.SearchScreen

private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: @Composable () -> Unit,
)

@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavItem(Screen.Home, "홈") { Icon(Icons.Default.Home, "홈") },
        BottomNavItem(Screen.Search, "검색") { Icon(Icons.Default.Search, "검색") },
        BottomNavItem(Screen.Saved, "찜") { Icon(Icons.Default.Bookmark, "찜") },
        BottomNavItem(Screen.Activity, "내 활동") { Icon(Icons.Default.Timeline, "내 활동") },
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = item.icon,
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Saved.route) { SavedScreen() }
            composable(Screen.Activity.route) {
                ActivityScreen(
                    onNavigateToSettings = onNavigateToSettings
                )
            }
        }
    }
}