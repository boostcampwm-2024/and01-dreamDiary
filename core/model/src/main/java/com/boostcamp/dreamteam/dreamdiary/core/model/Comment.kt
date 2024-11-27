package com.boostcamp.dreamteam.dreamdiary.core.model

data class Comment(
    val id: String,
    val author: Author,
    val content: String,
    val likes: Int,
    val createdAt: Long,
)
