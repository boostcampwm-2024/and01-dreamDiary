package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.database.CommentDataSource
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import kotlinx.coroutines.flow.Flow
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
        ).flow
    }

    suspend fun addComment(postId: String, comment: Comment): Boolean {
        return commentDataSource.addComment(postId, comment)
    }
}
