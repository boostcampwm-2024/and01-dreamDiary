package com.boostcamp.dreamteam.dreamdiary.community.model

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import kotlinx.coroutines.flow.flowOf
import java.time.Instant

data class CommentUi(
    val id: String,
    val author: UserUi,
    val content: String,
    val isLiked: Boolean,
    val createdAt: DisplayableDateTime,
)

fun Comment.toUIState(): CommentUi =
    CommentUi(
        id = this.id,
        author = UserUi(
            uid = this.uid,
            username = this.author,
            profileImageUrl = this.profileImageUrl,
        ),
        content = this.content,
        isLiked = true,
        createdAt = Instant.ofEpochSecond(this.createdAt).toDisplayableDateTime(),
    )

internal val commentUiPreview1 = CommentUi(
    id = "1",
    author = userUiPreview1,
    content = """
        댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글
        내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용댓글 내용
    """.trimIndent(),
    isLiked = true,
    createdAt = Instant.ofEpochSecond(1733155061).toDisplayableDateTime(),
)

internal val commentUiPreview2 = CommentUi(
    id = "2",
    author = userUiPreview2,
    content = "댓글 내용",
    isLiked = false,
    createdAt = Instant.ofEpochSecond(1733165061).toDisplayableDateTime(),
)

private val commentUiPreview3 = CommentUi(
    id = "3",
    author = userUiPreview3,
    content = "댓글 내용",
    isLiked = true,
    createdAt = Instant.ofEpochSecond(1733126000).toDisplayableDateTime(),
)

internal val commentsUiPreview = listOf(
    commentUiPreview1,
    commentUiPreview2,
    commentUiPreview3,
)

internal val pagingCommentsUiPreview = flowOf(PagingData.from(commentsUiPreview))
