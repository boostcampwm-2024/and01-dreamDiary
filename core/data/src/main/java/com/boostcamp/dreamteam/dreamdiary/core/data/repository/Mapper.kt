package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryWithLabels
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

fun DreamDiaryEntity.toDomain(diaryContents: List<DiaryContent> = listOf()): Diary {
    return Diary(
        id = this.id,
        title = this.title,
        content = this.body,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        images = diaryContents.filterIsInstance<DiaryContent.Image>().map { it.path },
        labels = listOf(),
        sleepStartAt = this.sleepStartAt,
        sleepEndAt = this.sleepEndAt,
        diaryContents = diaryContents,
    )
}

fun LabelEntity.toDomain(): Label {
    return Label(
        name = this.label,
    )
}

fun DreamDiaryWithLabels.toDomain(diaryContents: List<DiaryContent> = listOf()): Diary {
    return Diary(
        id = dreamDiary.id,
        title = dreamDiary.title,
        content = dreamDiary.body,
        createdAt = dreamDiary.createdAt,
        updatedAt = dreamDiary.updatedAt,
        images = diaryContents.filterIsInstance<DiaryContent.Image>().map { it.path },
        labels = this.labels.map { it.toDomain() },
        sleepStartAt = dreamDiary.sleepStartAt,
        sleepEndAt = dreamDiary.sleepEndAt,
        diaryContents = diaryContents,
    )
}
