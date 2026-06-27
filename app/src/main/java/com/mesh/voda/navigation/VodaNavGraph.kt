package com.mesh.voda.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mesh.voda.presentation.auth.login.LoginScreen
import com.mesh.voda.presentation.auth.signup.SignupScreen
import com.mesh.voda.presentation.main.MainScreen
import com.mesh.voda.presentation.onboarding.OnboardingScreen

@Composable
fun VodaNavGraph(
    rootViewModel: RootViewModel = hiltViewModel()
) {
    val startDestination by rootViewModel.startDestination.collectAsState()

    // 시작 목적지 준비 전에는 로딩만 표시(온보딩→로그인 깜빡임 방지).
    val start = startDestination ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                // Mock: 구글 로그인 → 프로필 설정(회원가입) 플로우로 진입
                onGoogleStart = { navController.navigate(Screen.Signup.route) }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            MainScreen()
        }
    }
}
