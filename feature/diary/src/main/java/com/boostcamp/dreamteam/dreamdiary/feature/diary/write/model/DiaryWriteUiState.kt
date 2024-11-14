package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import java.time.ZonedDateTime

data class DiaryWriteUiState(
    val title: String = "",
    val content: String = "",
    val searchValue: String = "",
    val selectableLabels: List<SelectableLabel> = emptyList(),
    val sleepEndAt: ZonedDateTime = ZonedDateTime.now(),
    val sleepStartAt: ZonedDateTime = sleepEndAt.minusHours(6),
)

data class SelectableLabel(
    val label: LabelUi,
    val isSelected: Boolean,
)
