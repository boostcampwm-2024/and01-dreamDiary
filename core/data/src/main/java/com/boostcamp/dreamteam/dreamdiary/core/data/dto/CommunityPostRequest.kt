package com.boostcamp.dreamteam.dreamdiary.core.data.dto

data class CommunityPostRequest(
    val author: String,
    val title: String,
    val content: String,
    val labels: List<String> = emptyList(),
    val sleepStartAt: Long,
    val sleepEndAt: Long,
    val images: List<String> = emptyList(),
)
