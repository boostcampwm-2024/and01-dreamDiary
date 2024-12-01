package com.boostcamp.dreamteam.dreamdiary.core.model.community

import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import java.time.Instant

data class CommunityPostList(
    val id: String,
    val uid: String,
    val author: String,
    val profileImageUrl: String,
    val title: String,
    val commentCount: Long,
    val likeCount: Long,
    val isLiked: Boolean,
    val diaryContents: List<DiaryContent>,
    val createdAt: Instant,
)
