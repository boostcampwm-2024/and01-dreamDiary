package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toLabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import java.time.ZoneId
import java.time.ZonedDateTime

data class DiaryDetailUiState(
    val diaryUIState: DiaryUiState = DiaryUiState(),
    val loading: Boolean = true,
)

data class DiaryUiState(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val labels: List<LabelUi> = listOf(),
    val sleepStartAt: ZonedDateTime = ZonedDateTime.now(),
    val sleepEndAt: ZonedDateTime = ZonedDateTime.now(),
    val diaryContents: List<DiaryContentUi> = listOf()
)

fun Diary.toUIState(): DiaryUiState {
    return DiaryUiState(
        id = this.id,
        title = this.title,
        labels = this.labels.map { it.toLabelUi() },
        content = this.content,
        sleepStartAt = this.sleepStartAt.atZone(ZoneId.systemDefault()),
        sleepEndAt = this.sleepEndAt.atZone(ZoneId.systemDefault()),
        diaryContents = this.diaryContents.map { it.toUiState() }
    )
}
