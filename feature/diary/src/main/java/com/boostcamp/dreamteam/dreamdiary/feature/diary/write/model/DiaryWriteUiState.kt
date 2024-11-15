package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import java.time.ZonedDateTime

data class DiaryWriteUiState(
    val title: String = "",
    val content: String = "",
    val labelFilter: String = "",
    val filteredLabels: List<LabelUi> = emptyList(),
    val selectedLabels: Set<LabelUi> = emptySet(),
    val sleepEndAt: ZonedDateTime = ZonedDateTime.now(),
    val sleepStartAt: ZonedDateTime = sleepEndAt.minusHours(6),
)
