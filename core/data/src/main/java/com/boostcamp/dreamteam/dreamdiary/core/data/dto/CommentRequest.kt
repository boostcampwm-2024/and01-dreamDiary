package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import java.time.Instant

data class CommentRequest(
    val content: String,
    val author: Author,
    val createdAt: Long = Instant.now().toEpochMilli(),
)

fun CommentRequest.toDomain(author: Author): Comment {
    return Comment(
        id = "",
        author = author,
        content = this.content,
        likes = 0,
        createdAt = this.createdAt
    )
}
