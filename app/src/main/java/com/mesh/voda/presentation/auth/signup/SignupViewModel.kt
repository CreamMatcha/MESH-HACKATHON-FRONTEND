package com.mesh.voda.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesh.voda.data.auth.AuthRepository
import com.mesh.voda.data.auth.model.Interest
import com.mesh.voda.data.auth.model.KoreaRegions
import com.mesh.voda.data.auth.model.Region
import com.mesh.voda.data.auth.model.SignupRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 회원가입 단계. 순서대로 진행된다. 활동 지역은 봉사 성향 화면 안에서 팝업으로 선택한다. */
enum class SignupStep { BASIC_INFO, INTERESTS, TENDENCY }

const val REGION_METHOD_CUSTOM = "지역 직접 설정"

// Mock: 구글 로그인으로 받아온 것처럼 채워둔 더미 프로필. (실제 SDK 연동 시 교체)
private const val MOCK_GOOGLE_NAME = "김봉사"
private const val MOCK_GOOGLE_EMAIL = "kangminji@gmail.com"

data class SignupUiState(
    val step: SignupStep = SignupStep.BASIC_INFO,
    // Step 1 — 프로필 (구글 정보 자동 채움)
    val nickname: String = MOCK_GOOGLE_NAME,
    val email: String = MOCK_GOOGLE_EMAIL,
    val selectedAvatar: Int = 0,
    // Step 2 — 관심 분야
    val interests: Set<Interest> = emptySet(),
    // Step 3 — 봉사 성향 (추천 파트 연동 예정 — 현재는 선택값만 보관)
    val activityTime: String? = null,
    val activityFrequency: String? = null,
    val activityPlace: String? = null,
    val activityGroupType: String? = null,
    val activityRegionMethod: String? = null,
    // 활동 지역 — 봉사 성향 화면의 "지역 직접 설정" 선택 시 팝업으로 고른다.
    val selectedSido: Region = Region.SEOUL,
    val regions: Set<String> = emptySet(),
    val isRegionPickerOpen: Boolean = false,
    // 공통
    val isLoading: Boolean = false,
    val isComplete: Boolean = false,
    val errorMessage: String? = null,
) {
    val isNicknameValid: Boolean
        get() = nickname.trim().length in 2..10

    /** 현재 단계에서 '다음/완료' 버튼을 누를 수 있는지. */
    val canProceed: Boolean
        get() = when (step) {
            SignupStep.BASIC_INFO -> isNicknameValid
            SignupStep.INTERESTS -> interests.isNotEmpty()
            SignupStep.TENDENCY ->
                activityTime != null && activityFrequency != null &&
                    activityPlace != null && activityGroupType != null && activityRegionMethod != null &&
                    (activityRegionMethod != REGION_METHOD_CUSTOM || regions.isNotEmpty())
        } && !isLoading
}

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    // --- Step 1: 프로필 ---
    fun onNicknameChange(value: String) {
        if (value.length <= 10) _uiState.update { it.copy(nickname = value, errorMessage = null) }
    }

    fun selectAvatar(index: Int) = _uiState.update { it.copy(selectedAvatar = index) }

    // --- Step 2 ---
    fun toggleInterest(interest: Interest) = _uiState.update {
        val updated = when {
            interest in it.interests -> it.interests - interest
            it.interests.size >= MAX_INTERESTS -> it.interests // 최대 개수 도달 시 무시
            else -> it.interests + interest
        }
        it.copy(interests = updated)
    }

    companion object {
        const val MAX_INTERESTS = 3
    }

    // --- Step 3 ---
    fun selectActivityTime(value: String) = _uiState.update { it.copy(activityTime = value) }
    fun selectActivityFrequency(value: String) = _uiState.update { it.copy(activityFrequency = value) }
    fun selectActivityPlace(value: String) = _uiState.update { it.copy(activityPlace = value) }
    fun selectActivityGroupType(value: String) = _uiState.update { it.copy(activityGroupType = value) }
    fun selectActivityRegionMethod(value: String) = _uiState.update {
        it.copy(activityRegionMethod = value, isRegionPickerOpen = value == REGION_METHOD_CUSTOM)
    }

    // --- 활동 지역 직접 설정 팝업 ---
    fun openRegionPicker() = _uiState.update { it.copy(isRegionPickerOpen = true) }
    fun closeRegionPicker() = _uiState.update { it.copy(isRegionPickerOpen = false) }

    fun selectSido(sido: Region) = _uiState.update { it.copy(selectedSido = sido) }

    fun toggleDistrict(gugun: String) = _uiState.update {
        val full = KoreaRegions.fullName(it.selectedSido, gugun)
        val updated = if (full in it.regions) it.regions - full else it.regions + full
        it.copy(regions = updated)
    }

    fun removeRegion(fullName: String) = _uiState.update {
        it.copy(regions = it.regions - fullName)
    }

    /** '다음' 또는 마지막 단계에서 '가입 완료'. */
    fun onNext() {
        val state = _uiState.value
        if (!state.canProceed) return
        when (state.step) {
            SignupStep.BASIC_INFO -> _uiState.update { it.copy(step = SignupStep.INTERESTS) }
            SignupStep.INTERESTS -> _uiState.update { it.copy(step = SignupStep.TENDENCY) }
            SignupStep.TENDENCY -> submit()
        }
    }

    /** '뒤로'. 첫 단계면 true를 반환해 화면이 이전 라우트로 빠져나가게 한다. */
    fun onBack(): Boolean {
        val state = _uiState.value
        val prev = when (state.step) {
            SignupStep.BASIC_INFO -> return true // 화면을 닫아야 함
            SignupStep.INTERESTS -> SignupStep.BASIC_INFO
            SignupStep.TENDENCY -> SignupStep.INTERESTS
        }
        _uiState.update { it.copy(step = prev, errorMessage = null) }
        return false
    }

    private fun submit() {
        val state = _uiState.value
        val request = SignupRequest(
            name = state.nickname.trim(),
            email = state.email,
            avatar = state.selectedAvatar,
            interests = state.interests,
            regions = state.regions,
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            authRepository.signup(request)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isComplete = true) } }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "회원가입에 실패했습니다.")
                    }
                }
        }
    }
}
