package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.Author

data class CommentRequest(
    val id: String,
    val content: String,
    val author: Author,
    val likeCount: Int,
    val createdAt: Any,
)
