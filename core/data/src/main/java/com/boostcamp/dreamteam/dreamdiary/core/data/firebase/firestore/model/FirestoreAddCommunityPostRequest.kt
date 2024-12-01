package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.firestore.model

import com.google.firebase.firestore.FieldValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    @Transient
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
)
