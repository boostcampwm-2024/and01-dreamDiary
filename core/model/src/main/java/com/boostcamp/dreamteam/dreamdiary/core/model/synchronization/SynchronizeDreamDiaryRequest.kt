package com.boostcamp.dreamteam.dreamdiary.core.model.synchronization

import java.time.Instant

data class SynchronizeDreamDiaryRequest(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val sleepStartAt: Instant,
    val sleepEndAt: Instant,
    val labels: List<String>,
    val diaryContents: List<ContentId>,
    val previousVersion: String,
    val currentVersion: String,
) {
    sealed class ContentId {
        data class Image(val id: String) : ContentId()

        data class Text(val id: String) : ContentId()
    }
}
