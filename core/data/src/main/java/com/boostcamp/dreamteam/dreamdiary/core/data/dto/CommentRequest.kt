package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import java.time.Instant

data class CommentRequest(
    val id: String,
    val content: String,
    val author: Author,
    val likes: Int,
    val createdAt: Any,
)

fun CommentRequest.toDomain(): Comment {
    return Comment(
        id = "",
        author = this.author,
        content = this.content,
        likes = 0,
        createdAt = Instant.now().toEpochMilli(),
    )
}
