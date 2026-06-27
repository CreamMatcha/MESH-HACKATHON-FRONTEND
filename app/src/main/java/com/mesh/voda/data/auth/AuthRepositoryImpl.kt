package com.mesh.voda.data.auth

import com.mesh.voda.data.auth.model.AuthUser
import com.mesh.voda.data.auth.model.SignupRequest
import com.mesh.voda.data.local.AuthPreferences
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    // TODO: private val api: AuthApi, // Retrofit 서비스 연결 시 주입
    private val authPreferences: AuthPreferences,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthUser> {
        delay(MOCK_DELAY_MS)
        // TODO: api.login(LoginRequest(email, password)) 로 교체
        val user = AuthUser(
            id = UUID.randomUUID().toString(),
            email = email,
            nickname = email.substringBefore("@"),
        )
        authPreferences.saveAuthToken(MOCK_TOKEN)
        return Result.success(user)
    }

    override suspend fun signup(request: SignupRequest): Result<AuthUser> {
        delay(MOCK_DELAY_MS)
        // TODO: api.signup(request) 로 교체
        val user = AuthUser(
            id = UUID.randomUUID().toString(),
            email = request.email,
            nickname = request.name,
        )
        authPreferences.saveAuthToken(MOCK_TOKEN)
        return Result.success(user)
    }

    companion object {
        private const val MOCK_DELAY_MS = 800L
        private const val MOCK_TOKEN = "mock-access-token"
    }
}
