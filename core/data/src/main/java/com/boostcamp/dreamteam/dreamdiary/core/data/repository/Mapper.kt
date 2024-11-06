package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary

fun DreamDiaryEntity.toDomain(): Diary {
    return Diary(
        id = this.id,
        title = this.title,
        content = this.body,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        images = listOf(),
        labels = listOf(),
    )
}
