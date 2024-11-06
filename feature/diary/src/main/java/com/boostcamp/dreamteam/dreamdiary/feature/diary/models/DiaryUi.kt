package com.boostcamp.dreamteam.dreamdiary.feature.diary.models

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.vos.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.vos.toDisplayableDateTime
import java.time.Instant

data class DiaryUi(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: DisplayableDateTime,
    val updatedAt: DisplayableDateTime,
    val images: List<String>,
    val labels: List<LabelUi>,
)

// FIXME: 시간과 관련된 데이터를 처리하는 로직은 수정 해주세요
internal fun Diary.toDiaryUi(): DiaryUi =
    DiaryUi(
        id = id,
        title = title,
        content = content,
        createdAt = Instant.now().toDisplayableDateTime(),
        updatedAt = Instant.now().toDisplayableDateTime(),
        images = images,
        labels = labels.map { it.toLabelUi() },
    )
