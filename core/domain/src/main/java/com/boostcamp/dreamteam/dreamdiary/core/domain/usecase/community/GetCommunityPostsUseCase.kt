package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityPostsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(): Flow<PagingData<CommunityDreamPost>> {
        return communityRepository.getCommunityPosts()
    }
}
