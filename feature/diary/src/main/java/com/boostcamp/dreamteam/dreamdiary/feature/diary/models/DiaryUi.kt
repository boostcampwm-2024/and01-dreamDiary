package com.boostcamp.dreamteam.dreamdiary.feature.diary.models

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.vos.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.vos.toDisplayableDateTime

data class DiaryUi(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: DisplayableDateTime,
    val updatedAt: DisplayableDateTime,
    val images: List<String>,
    val labels: List<LabelUi>,
)

internal fun Diary.toDiaryUi(): DiaryUi =
    DiaryUi(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toDisplayableDateTime(),
        updatedAt = updatedAt.toDisplayableDateTime(),
        images = images,
        labels = labels.map { it.toLabelUi() },
    )
