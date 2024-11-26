package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommentRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommentRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(
        postId: String,
        content: String,
    ) {
        val userName = "author"
        val profileImageUrl = "https://picsum.photos/200/300"
        commentRepository.addComment(
            postId = postId,
            commentRequest = CommentRequest(
                content = content,
                author = Author(
                    userName = userName,
                    profileImageUrl = profileImageUrl,
                ),
            ),
        )
    }
}
