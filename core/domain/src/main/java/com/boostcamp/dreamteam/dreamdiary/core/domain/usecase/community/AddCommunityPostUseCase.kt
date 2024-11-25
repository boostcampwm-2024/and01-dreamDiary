package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import java.time.Instant
import javax.inject.Inject

class AddCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    suspend operator fun invoke(
        author: String,
        title: String,
        content: String,
    ): Boolean {
        return communityRepository.saveCommunityPost(
            CommunityDreamPost(
                author = author,
                title = title,
                content = content,
                // todo: 수정하기
                sleepStartAt = Instant.now(),
                sleepEndAt = Instant.now(),
                createdAt = Instant.now(),
                id = "ididid",
                labels = emptyList(),
            ),
        )
    }
}
