package com.boostcamp.dreamteam.dreamdiary.core.data.dto

data class CommentRequest(
    val authorId: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
