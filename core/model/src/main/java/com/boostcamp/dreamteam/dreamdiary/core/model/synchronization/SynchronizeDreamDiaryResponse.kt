package com.boostcamp.dreamteam.dreamdiary.core.model.synchronization

data class SynchronizeDreamDiaryResponse(
    val currentVersion: String,
    val newDiary: NewDiary?,
    val updateDiary: UpdateDiary?,
    val deletedDiary: DeletedDiary?,
) {
    data class NewDiary(
        val createdAt: Long,
        val labels: List<String>,
        val sleepEndAt: Long,
        val sleepStartAt: Long,
        val title: String,
        val updatedAt: Long,
        val content: String,
    )
    data class UpdateDiary(
        val createdAt: Long,
        val labels: List<String>,
        val sleepEndAt: Long,
        val sleepStartAt: Long,
        val title: String,
        val updatedAt: Long,
        val content: String,
    )
    data class DeletedDiary(
        val deleted: Boolean,
    )
}
