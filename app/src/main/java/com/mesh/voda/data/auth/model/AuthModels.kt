package com.mesh.voda.data.auth.model

/** 공통 API 응답 래퍼. */
data class ApiWrapper<T>(
    val status: Int,
    val message: String,
    val data: T?,
)

/** Google 로그인 요청 body. */
data class GoogleLoginRequest(
    val idToken: String,
)

/** Google 로그인 성공 시 서버에서 받는 데이터. */
data class GoogleLoginApiData(
    val accessToken: String,
    val isNewUser: Boolean,
)

/** 로그인/회원가입 성공 후 받는 사용자 정보. */
data class AuthUser(
    val id: String,
    val email: String,
    val nickname: String,
)

/** 회원가입에서 수집한 가입 요청 데이터. (구글 로그인 기반 — 비밀번호 없음) */
data class SignupRequest(
    val name: String,
    val email: String,
    val avatar: Int,
    val interests: Set<Interest>,
    /** "서울 강남구" 형태의 구·군 단위 활동 지역. */
    val regions: Set<String>,
    // TODO: 봉사 성향(MBTI) — 추천 파트 연동 시 필드 추가
)
