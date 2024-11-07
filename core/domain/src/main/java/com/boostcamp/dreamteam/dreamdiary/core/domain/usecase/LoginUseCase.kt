package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import android.content.Intent
import com.boostcamp.dreamteam.dreamdiary.core.data.database.GoogleLogInDataSource
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val googleLogInDataSource: GoogleLogInDataSource,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(data: Intent?): Result<Unit> {
        return try {
            val token = googleLogInDataSource.handleSignInResult(data)
            if (token != null) {
                authRepository.saveAuthToken(token)  // 토큰 저장
                Result.success(Unit)
            } else {
                Result.failure(Exception("Google sign-in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
