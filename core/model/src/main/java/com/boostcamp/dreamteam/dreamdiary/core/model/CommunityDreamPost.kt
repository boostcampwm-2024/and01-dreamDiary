package com.boostcamp.dreamteam.dreamdiary.core.model

data class CommunityDreamPost(
    val id: String = "",
    val author: String = "",
    val title: String = "",
    val content: String = "",
    val labels: List<Label> = emptyList(),
    val likes: Int = 0,
    val commentCount: Int = 0,
    val sleepStartAt: Long = 0L,
    val sleepEndAt: Long = 0L,
    val images: List<String> = emptyList(),
    val createdAt: Long = 0L,
)
