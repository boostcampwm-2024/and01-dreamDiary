package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

data class DiaryWriteUiState(
    val title: String = "",
    val content: String = "",
    val searchValue: String = "",
    val labels: List<String> = emptyList(),
    val selectedLabels: List<Boolean> = emptyList(),
)
