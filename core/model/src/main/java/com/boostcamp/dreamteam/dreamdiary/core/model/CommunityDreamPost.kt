package com.boostcamp.dreamteam.dreamdiary.core.model

data class CommunityDreamPost(
    val id: String,
    val author: String,
    val title: String,
    val content: String,
    val labels: List<Label>,
    val likes: Int,
    val commentCount: Int,
    val sleepStartAt: Long,
    val sleepEndAt: Long,
    val images: List<String>,
    val createdAt: Long,
)
