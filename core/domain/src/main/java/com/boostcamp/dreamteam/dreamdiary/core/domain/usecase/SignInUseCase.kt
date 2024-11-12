package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import android.credentials.Credential
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(account: Credential): Result<Unit> {
        return Result.success(Unit)
    }
}
