package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommunityRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import javax.inject.Inject

class AddCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        title: String,
        diaryContents: List<DiaryContent>,
    ): String {
        val uid = authRepository.getUserUID() ?: throw Exception()
        val userName = authRepository.getUserName() ?: throw Exception()

        return communityRepository.saveCommunityPost(
            title = title,
            diaryContents = diaryContents,
            uid = uid,
            name = userName,
        )
    }
}
