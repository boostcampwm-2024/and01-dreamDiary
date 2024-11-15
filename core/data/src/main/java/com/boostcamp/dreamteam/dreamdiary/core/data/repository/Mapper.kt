package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryWithLabels
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

fun DreamDiaryEntity.toDomain(): Diary {
    return Diary(
        id = this.id,
        title = this.title,
        content = this.body,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        images = listOf(),
        labels = listOf(),
        sleepStartAt = this.sleepStartAt,
        sleepEndAt = this.sleepEndAt,
    )
}

fun LabelEntity.toDomain(): Label {
    return Label(
        name = this.label,
    )
}

fun DreamDiaryWithLabels.toDomain(): Diary {
    return Diary(
        id = dreamDiary.id,
        title = dreamDiary.title,
        content = dreamDiary.body,
        createdAt = dreamDiary.createdAt,
        updatedAt = dreamDiary.updatedAt,
        images = listOf(),
        labels = this.labels.map { it.toDomain() },
        sleepStartAt = dreamDiary.sleepStartAt,
        sleepEndAt = dreamDiary.sleepEndAt,
    )
}
