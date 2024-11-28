package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityPostDetail
import javax.inject.Inject

class GetCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    suspend operator fun invoke(postId: String): CommunityPostDetail {
        return communityRepository.getCommunityPostById(postId)
    }
}
