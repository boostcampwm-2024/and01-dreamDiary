package com.boostcamp.dreamteam.dreamdiary.community.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf

data class CommentUi(
    val id: String,
    val author: UserUi,
    val contents: String,
    val isLiked: Boolean,
)

internal val commentUiPreview1 = CommentUi(
    id = "1",
    author = userUiPreview1,
    contents = "댓글 내용",
    isLiked = true,
)

internal val commentUiPreview2 = CommentUi(
    id = "2",
    author = userUiPreview2,
    contents = "댓글 내용",
    isLiked = false,
)

private val commentUiPreview3 = CommentUi(
    id = "3",
    author = userUiPreview3,
    contents = "댓글 내용",
    isLiked = true,
)

internal val commentsUiPreview = listOf(
    commentUiPreview1,
    commentUiPreview2,
    commentUiPreview3,
)

internal val pagingCommentsUiPreview = flowOf(PagingData.from(commentsUiPreview))
