package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

data class CommunityPostResponse(
    val id: String = "",
    val author: String = "",
    val title: String = "",
    val content: String = "",
    val labels: List<String> = emptyList(),
    val likes: Int = 0,
    val commentCount: Int = 0,
    val sleepStartAt: Long = 0L,
    val sleepEndAt: Long = 0L,
    val images: List<String> = emptyList(),
    val createdAt: Long = 0L,
)

fun CommunityPostResponse.toDomain(): CommunityDreamPost {
    return CommunityDreamPost(
        id = this.id,
        author = this.author,
        title = this.title,
        content = this.content,
        labels = this.labels.map { Label(it) },
        likes = this.likes,
        commentCount = this.commentCount,
        sleepStartAt = this.sleepStartAt,
        sleepEndAt = this.sleepEndAt,
        images = this.images,
        createdAt = this.createdAt,
    )
}
