package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import javax.inject.Inject

class DeleteCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    suspend operator fun invoke(postId: String) {
        communityRepository.deleteCommunityPost(postId)
    }
}
