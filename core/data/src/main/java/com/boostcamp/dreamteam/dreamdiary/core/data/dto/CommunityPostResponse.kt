package com.boostcamp.dreamteam.dreamdiary.core.data.dto

import com.google.firebase.Timestamp

data class CommunityPostResponse(
    val id: String = "",
    val uid: String = "",
    val author: String = "",
    val profileImageUrl: String = "",
    val title: String = "",
    val content: String = "",
    val likes: Int = 0,
    val commentCount: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
)
