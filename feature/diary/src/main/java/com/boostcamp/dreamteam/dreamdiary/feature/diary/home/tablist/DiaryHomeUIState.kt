package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist

import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.DiaryUi

data class DiaryHomeUIState(
    val diaries: List<DiaryUi> = listOf(),
    val loading: Boolean = false,
)
