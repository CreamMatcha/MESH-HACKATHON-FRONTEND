package com.mesh.voda.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mesh.voda.R // 본인 프로젝트 패키지의 R 리소스 임포트
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
        BottomNavItem(Screen.Home, "홈") {
            Icon(painter = painterResource(id = R.drawable.ic_nav_home), contentDescription = "홈")
        },
        BottomNavItem(Screen.Search, "검색") {
            Icon(painter = painterResource(id = R.drawable.ic_nav_search), contentDescription = "검색")
        },
        BottomNavItem(Screen.Saved, "찜") {
            Icon(painter = painterResource(id = R.drawable.ic_nav_saved), contentDescription = "찜")
        },
        BottomNavItem(Screen.Activity, "내 활동") {
            Icon(painter = painterResource(id = R.drawable.ic_nav_activity), contentDescription = "내 활동")
        },
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            Column() {
                androidx.compose.material3.HorizontalDivider(
                    color = Color(0xFFEDE8DC),
                    thickness = 1.dp
                )

                NavigationBar(
                    containerColor = Color(0xFFFBF7EF)
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = item.icon,
                            label = { Text(item.label, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF4E8A3F),
                                selectedTextColor = Color(0xFF4E8A3F),
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = Color(0xFFBBBBBB),
                                unselectedTextColor = Color(0xFFBBBBBB)
                            )
                        )
                    }
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
                ActivityScreen()
            }
        }
    }
}