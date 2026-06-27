package com.mesh.voda.navigation

sealed class Screen(val route: String) {
    // Auth
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Signup : Screen("signup") // 3단계(기본정보→관심→지역)는 SignupScreen 내부에서 처리

    // Main (Bottom Nav)
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Saved : Screen("saved")
    data object Activity : Screen("activity")
    data object Settings : Screen("settings")

    // Map
    data object Map : Screen("map")

    // Detail
    data object Detail : Screen("detail")
}
