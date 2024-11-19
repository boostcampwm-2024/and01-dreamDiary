package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.serialization.Serializable

@Serializable
data class DreamDiarySync(
    val list: List<DreamDiary>
) {
    @Serializable
    data class DreamDiary(
        val id: String,
        val title: String,
        val createdAt: Long,
        val updatedAt: Long,
        val sleepStartAt: Long,
        val sleepEndAt: Long,
        val labels: List<String>,
        val version: Long,
    )
}

fun Diary.toDreamDiarySync(): DreamDiarySync.DreamDiary {
    return DreamDiarySync.DreamDiary(
        id = this.id,
        title = this.title,
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        sleepStartAt = this.sleepStartAt.toEpochMilli(),
        sleepEndAt = this.sleepEndAt.toEpochMilli(),
        labels = this.labels.map { it.name },
        version = this.version
    )
}
