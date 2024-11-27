package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommunityPostRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
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
            CommunityPostRequest(
                author = author,
                title = title,
                content = content,
                sleepStartAt = Instant.now().toEpochMilli(),
                sleepEndAt = Instant.now().toEpochMilli(),
            ),
        )
    }
}
