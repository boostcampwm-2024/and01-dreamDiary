package com.boostcamp.dreamteam.dreamdiary.core.data.dto

data class CommentRequest(
    val id: String,
    val content: String,
    val uid: String,
    val profileImageUrl: String,
    val author: String,
    val likeCount: Long,
    val createdAt: Any,
)
