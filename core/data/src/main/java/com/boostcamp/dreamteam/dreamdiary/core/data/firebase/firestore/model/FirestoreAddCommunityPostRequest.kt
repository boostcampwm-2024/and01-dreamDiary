package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.firestore.model

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreAddCommunityPostRequest(
    val id: String,
    val uid: String,
    val author: String,
    val profileImageUrl: String,
    val title: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Long,
)
