package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.firestore.model

import com.google.firebase.Timestamp

data class FirestoreGetCommunityPostResponse(
    val id: String = "",
    val uid: String = "",
    val author: String = "",
    val title: String = "",
    val content: String = "",
    val commentCount: Long = 0,
    val likeCount: Int = 0,
    val createdAt: Timestamp = Timestamp(0, 0),
)
