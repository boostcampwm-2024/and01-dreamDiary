package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import com.google.firebase.Timestamp

data class CommentResponse(
    val id: String = "",
    val uid: String = "",
    val author: String = "",
    val profileImageUrl: String = "",
    val content: String = "",
    val likeCount: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
)

fun CommentResponse.toDomain(): Comment {
    return Comment(
        id = this.id,
        uid = this.uid,
        author = this.author,
        profileImageUrl = this.profileImageUrl,
        content = this.content,
        likeCount = this.likeCount,
        createdAt = this.createdAt.seconds,
    )
}
