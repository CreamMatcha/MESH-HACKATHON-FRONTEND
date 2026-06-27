package com.mesh.voda.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesh.voda.data.local.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    authPreferences: AuthPreferences,
) : ViewModel() {

    /**
     * 시작 목적지. 온보딩 완료 여부에 따라 결정된다.
     * 준비 전에는 null(로딩) — 깜빡임을 막기 위해 null 동안에는 NavHost를 그리지 않는다.
     */
    val startDestination: StateFlow<String?> = authPreferences.onboardingCompleted
        .combine(authPreferences.authToken) { completed, token ->
            when {
                !completed -> Screen.Onboarding.route
                token != null -> Screen.Home.route
                else -> Screen.Login.route
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
