package com.boostcamp.dreamteam.dreamdiary.core.model.community

import java.time.Instant

data class CommunityPostList(
    val id: String,
    val author: String,
    val title: String,
    val createdAt: Instant,
)
