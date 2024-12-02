package com.boostcamp.dreamteam.dreamdiary.core.model

data class CommunityPostDetail(
    val id: String,
    val author: String,
    val profileImageUrl: String,
    val title: String,
    val content: String,
    val isLiked: Boolean,
    val uid: String,
    val likeCount: Int,
    val commentCount: Int,
    val postContents: List<DiaryContent>,
    val createdAt: Long,
)
