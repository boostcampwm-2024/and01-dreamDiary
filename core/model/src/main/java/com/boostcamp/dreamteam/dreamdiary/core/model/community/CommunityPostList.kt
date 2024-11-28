package com.boostcamp.dreamteam.dreamdiary.core.model.community

import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import java.time.Instant

data class CommunityPostList(
    val id: String,
    val author: String,
    val title: String,
    val commentCount: Long,
    val diaryContents: List<DiaryContent>,
    val createdAt: Instant,
)
