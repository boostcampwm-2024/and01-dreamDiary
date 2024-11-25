package com.boostcamp.dreamteam.dreamdiary.core.model

import java.time.Instant

data class Comment(
    val id: String,
    val author: String,
    val content: String,
    val likes: Int = 0,
    val createdAt: Instant,
)
