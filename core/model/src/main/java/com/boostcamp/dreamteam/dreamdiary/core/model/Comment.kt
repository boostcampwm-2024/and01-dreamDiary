package com.boostcamp.dreamteam.dreamdiary.core.model

data class Comment(
    val id: String,
    val uid: String,
    val author: String,
    val profileImageUrl: String,
    val content: String,
    val likeCount: Long,
    val createdAt: Long,
)
