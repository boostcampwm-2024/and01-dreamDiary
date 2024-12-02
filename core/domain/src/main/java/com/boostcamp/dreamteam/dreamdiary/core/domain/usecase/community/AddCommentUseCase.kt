package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommentRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        postId: String,
        content: String,
    ) {
        val uid = authRepository.getUserUID() ?: throw Exception()
        val userName = authRepository.getUserName() ?: throw Exception()
        val profileImageUrl = authRepository.getUserPhotoUrl() ?: throw Exception()

        commentRepository.saveComment(
            postId = postId,
            content = content,
            author = Author(
                id = uid,
                userName = userName,
                profileImageUrl = profileImageUrl.toString(),
            ),
        )
    }
}
