package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist

import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi

data class DiaryHomeTabListUIState(
    val diaries: List<DiaryUi> = listOf(),
    val loading: Boolean = false,
)

data class DiarySort(
    val type: DiarySortType,
    val order: DiarySortOrder,
)

enum class DiarySortType {
    CREATED,
    UPDATED,
    SLEEP,
}

enum class DiarySortOrder {
    ASC,
    DESC,
}
