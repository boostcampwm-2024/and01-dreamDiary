package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        postId: String,
        commentId: String,
    ) {
        commentRepository.deleteComment(
            postId = postId,
            commentId = commentId,
        )
    }
}
