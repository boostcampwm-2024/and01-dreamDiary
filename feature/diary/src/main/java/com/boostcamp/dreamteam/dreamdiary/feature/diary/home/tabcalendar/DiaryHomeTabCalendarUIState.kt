package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar

import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import java.time.YearMonth

data class DiaryHomeTabCalendarUIState(
    val yearMonth: YearMonth = YearMonth.now(),
    val diariesOfMonth: List<DiaryUi> = listOf(),
)
