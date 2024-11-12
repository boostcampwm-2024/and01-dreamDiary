package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            authRepository.signInWithGoogle()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
