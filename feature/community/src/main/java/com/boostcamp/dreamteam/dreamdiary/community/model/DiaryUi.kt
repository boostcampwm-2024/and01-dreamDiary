package com.boostcamp.dreamteam.dreamdiary.community.model

data class DiaryUi(
    val id: String,
)

internal val diaryUiPreview1 = DiaryUi(
    id = "1",
)

private val diaryUiPreview2 = DiaryUi(
    id = "2",
)

private val diaryUiPreview3 = DiaryUi(
    id = "3",
)

internal val diariesUiPreview = listOf(
    diaryUiPreview1,
    diaryUiPreview2,
    diaryUiPreview3,
)
