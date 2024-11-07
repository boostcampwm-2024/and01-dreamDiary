package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.LabelUi

data class DiaryWriteUiState(
    val title: String = "",
    val content: String = "",
    val searchValue: String = "",
    val selectableLabels: List<SelectableLabel> = emptyList(),
)

data class SelectableLabel(
    val label: LabelUi,
    val isSelected: Boolean,
)
