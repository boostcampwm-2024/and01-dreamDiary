package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SynchronizeDreamDiaryRequest
import kotlinx.serialization.Serializable

@Serializable
data class FunctionsSynchronizeDiaryRequest(
    val list: List<DreamDiary>,
) {
    @Serializable
    data class DreamDiary(
        val diaryId: String,
        val title: String,
        val content: String,
        val createdAt: Long,
        val updatedAt: Long,
        val deletedAt: Long?,
        val sleepStartAt: Long,
        val sleepEndAt: Long,
        val labels: List<String>,
        val previousVersion: String,
        val currentVersion: String,
    )
}

fun SynchronizeDreamDiaryRequest.toFunctionsRequest(): FunctionsSynchronizeDiaryRequest.DreamDiary {
    return FunctionsSynchronizeDiaryRequest.DreamDiary(
        diaryId = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        deletedAt = this.deletedAt?.toEpochMilli(),
        sleepStartAt = this.sleepStartAt.toEpochMilli(),
        sleepEndAt = this.sleepEndAt.toEpochMilli(),
        labels = this.labels,
        previousVersion = this.previousVersion,
        currentVersion = this.currentVersion,
    )
}
