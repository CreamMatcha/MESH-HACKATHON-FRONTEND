package com.mesh.voda.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesh.voda.data.local.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
) : ViewModel() {

    /** 온보딩을 다시 보지 않도록 완료 상태를 저장한다. */
    fun completeOnboarding() {
        viewModelScope.launch {
            authPreferences.setOnboardingCompleted()
        }
    }
}
