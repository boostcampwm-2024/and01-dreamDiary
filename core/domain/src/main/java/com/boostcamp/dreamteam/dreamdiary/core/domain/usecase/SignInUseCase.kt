package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import android.content.Intent
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(data: Intent?): Result<Unit> {
        return try {
            authRepository.signInWithGoogle(data)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
