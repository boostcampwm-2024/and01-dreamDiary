package com.boostcamp.dreamteam.dreamdiary.core.model

data class CommunityPostDetail(
    val id: String,
    val author: String,
    val profileImageUrl: String,
    val title: String,
    val content: String,
    val likes: Int,
    val commentCount: Int,
    val createdAt: Long,
)
