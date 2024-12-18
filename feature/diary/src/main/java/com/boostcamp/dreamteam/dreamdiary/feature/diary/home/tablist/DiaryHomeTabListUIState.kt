package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist

import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi

data class DiaryHomeTabListUIState(
    val diaries: List<DiaryUi> = listOf(),
    val loading: Boolean = false,
)
