package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommentRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    operator fun invoke(postId: String): Flow<PagingData<Comment>> {
        return commentRepository.getCommentsForPostPagingSource(postId)
    }
}
