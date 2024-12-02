package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toLabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.vo.toDisplayableDateTime
import java.time.ZonedDateTime

data class DiaryWriteUiState(
    val title: String = "",
    val labelFilter: String = "",
    val filteredLabels: List<LabelUi> = emptyList(),
    val selectedLabels: Set<LabelUi> = emptySet(),
    val sleepEndAt: ZonedDateTime = ZonedDateTime.now(),
    val sleepStartAt: ZonedDateTime = sleepEndAt.minusHours(6),
    val diaryContents: List<DiaryContentUi> = listOf(DiaryContentUi.Text("")),
)

internal fun Diary.toUiState() =
    DiaryWriteUiState(
        title = title,
        selectedLabels = labels.map { it.toLabelUi() }.toSet(),
        sleepStartAt = sleepStartAt.toDisplayableDateTime().value,
        sleepEndAt = sleepEndAt.toDisplayableDateTime().value,
        diaryContents = diaryContents.map { it.toUiState() },
    )
