package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityPostDetail
import javax.inject.Inject

class GetCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(postId: String): CommunityPostDetail {
        val userId = authRepository.getUserUID() ?: throw IllegalStateException("User is not signed in")
        return communityRepository.getCommunityPostById(
            postId = postId,
            userId = userId,
        )
    }
}
