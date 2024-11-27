package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.Comment

data class CommentResponse(
    val id: String = "",
    val author: String,
    val profileImageUrl: String,
    val content: String = "",
    val likeCount: Int = 0,
    val createdAt: Long = 0L,
)

fun CommentResponse.toDomain(): Comment {
    return Comment(
        id = this.id,
        author = this.author,
        profileImageUrl = this.profileImageUrl,
        content = this.content,
        likeCount = this.likeCount,
        createdAt = this.createdAt,
    )
}
