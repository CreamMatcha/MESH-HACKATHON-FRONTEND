package com.mesh.voda.navigation

sealed class Screen(val route: String) {
    // Auth
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object SignupCategory : Screen("signup/category")
    data object SignupRegion : Screen("signup/region")

    // Main (Bottom Nav)
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Saved : Screen("saved")
    data object Activity : Screen("activity")
    data object Settings : Screen("settings")
}
