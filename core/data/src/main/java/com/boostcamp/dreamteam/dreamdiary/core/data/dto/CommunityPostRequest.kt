package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

data class CommunityPostRequest(
    val author: String,
    val title: String,
    val content: String,
    val labels: List<String> = emptyList(),
    val sleepStartAt: Long,
    val sleepEndAt: Long,
    val images: List<String> = emptyList(),
)

// toDomain? toModel?
fun CommunityPostRequest.toDomain(): CommunityDreamPost {
    return CommunityDreamPost(
        id = "",
        author = this.author,
        title = this.title,
        content = this.content,
        labels = this.labels.map { Label(it) },
        likes = 0,
        commentCount = 0,
        sleepStartAt = this.sleepStartAt,
        sleepEndAt = this.sleepEndAt,
        images = this.images,
        createdAt = System.currentTimeMillis(),
    )
}
