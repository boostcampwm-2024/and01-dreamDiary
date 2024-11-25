package com.boostcamp.dreamteam.dreamdiary.core.model

import java.time.Instant

data class CommunityDreamPost(
    val author: String,
    val title: String,
    val content: String,
    val labels: List<Label> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val likes: Int = 0,
    val sleepStartAt: Instant,
    val sleepEndAt: Instant,
    val createdAt: Instant,
)
