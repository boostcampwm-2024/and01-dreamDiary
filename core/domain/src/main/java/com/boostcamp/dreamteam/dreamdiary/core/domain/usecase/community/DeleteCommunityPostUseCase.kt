package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import javax.inject.Inject

class DeleteCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(postId: String) {

        // 사용자 본인 여부 확인
        val uid = authRepository.getUserUID()

        communityRepository.deleteCommunityPost(postId)
    }
}
