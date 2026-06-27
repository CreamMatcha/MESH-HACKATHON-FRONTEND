package com.mesh.voda.data.auth

import com.mesh.voda.data.auth.model.AuthUser
import com.mesh.voda.data.auth.model.SignupRequest

/** 인증(로그인/회원가입) 도메인 진입점. */
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthUser>
    suspend fun signup(request: SignupRequest): Result<AuthUser>
}
