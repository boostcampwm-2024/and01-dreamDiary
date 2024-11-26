package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.database.CommentDataSource
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommentRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.toDomain
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentDataSource: CommentDataSource,
) {
    fun getCommentsForPostPagingSource(postId: String): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { commentDataSource.getCommentsForPostPagingSource(postId) },
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    suspend fun addComment(
        postId: String,
        commentRequest: CommentRequest,
    ): Boolean {
        return commentDataSource.addComment(postId, commentRequest)
    }
}
