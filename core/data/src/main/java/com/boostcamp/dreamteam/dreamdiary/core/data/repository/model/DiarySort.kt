package com.boostcamp.dreamteam.dreamdiary.core.data.repository.model

import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity

data class DiarySort(
    val type: DiarySortType,
    val order: DiarySortOrder,
)

enum class DiarySortType(
    val value: String,
) {
    CREATED(DreamDiaryEntity::createdAt.name),
    UPDATED(DreamDiaryEntity::updatedAt.name),
    SLEEP(DreamDiaryEntity::sleepEndAt.name),
}

enum class DiarySortOrder(
    val code: Int,
) {
    ASC(0),
    DESC(1),
}
