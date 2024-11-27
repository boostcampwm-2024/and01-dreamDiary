package com.boostcamp.dreamteam.dreamdiary.core.model

data class Comment(
    val id: String,
    val author: String,
    val profileImageUrl: String,
    val content: String,
    val likeCount: Int,
    val createdAt: Long,
)
