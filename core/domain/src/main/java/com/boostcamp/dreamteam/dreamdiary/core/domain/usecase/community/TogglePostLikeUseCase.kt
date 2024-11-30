package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import javax.inject.Inject

class TogglePostLikeUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(postId: String) {
        val userId = authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        communityRepository.togglePostLike(postId, userId)
    }
}
