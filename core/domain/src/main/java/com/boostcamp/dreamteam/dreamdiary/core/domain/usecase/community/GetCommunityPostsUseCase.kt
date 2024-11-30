package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.community.CommunityPostList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityPostsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<PagingData<CommunityPostList>> {
        val uid = authRepository.getUserUID()
        return communityRepository.getCommunityPosts(uid)
    }
}
