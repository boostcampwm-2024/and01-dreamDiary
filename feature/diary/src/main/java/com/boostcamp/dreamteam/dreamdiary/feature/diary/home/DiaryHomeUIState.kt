package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary

data class DiaryHomeUIState(
    val diaries: List<Diary> = listOf(),
    val loading: Boolean = false,
)
