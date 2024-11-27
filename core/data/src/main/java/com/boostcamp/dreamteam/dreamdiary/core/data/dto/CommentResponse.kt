package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment

data class CommentResponse(
    val id: String = "",
    // 이 부분은 어떻게 처리하지? / Author는 같아도 될 듯?
    val author: Author = Author("123", "testuser", "https://picsum.photos/200/300"),
    val content: String = "",
    val likeCount: Int = 0,
    val createdAt: Long = 0L,
)

fun CommentResponse.toDomain(): Comment {
    return Comment(
        id = this.id,
        author = this.author,
        content = this.content,
        likeCount = this.likeCount,
        createdAt = this.createdAt,
    )
}
