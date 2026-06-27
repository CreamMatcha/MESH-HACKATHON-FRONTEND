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
import com.mesh.voda.presentation.mbti.MbtiResultScreen
import com.mesh.voda.presentation.mbti.RecommendationScreen
import com.mesh.voda.presentation.settings.SettingsScreen
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

        // 💡 에러 해결 지점: LoginScreen 파라미터 스펙 정합성 동기화
        composable(Screen.Login.route) {
            LoginScreen(
                // 상대방이 새로 설계한 구글 로그인 진입 인터페이스 콜백 연결
                onGoogleStart = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupComplete = { navController.navigate(Screen.MbtiResult.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.MbtiResult.route) {
            MbtiResultScreen(
                onSeeRecommendations = { navController.navigate(Screen.Recommendation.route) }
            )
        }
        composable(Screen.Recommendation.route) {
            RecommendationScreen(
                onGoHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            MainScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}