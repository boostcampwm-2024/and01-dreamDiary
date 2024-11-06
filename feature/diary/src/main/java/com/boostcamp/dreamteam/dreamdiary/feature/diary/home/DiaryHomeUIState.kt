package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.DiaryUi

data class DiaryHomeUIState(
    val diaries: List<DiaryUi> = listOf(),
    val loading: Boolean = false,
)
