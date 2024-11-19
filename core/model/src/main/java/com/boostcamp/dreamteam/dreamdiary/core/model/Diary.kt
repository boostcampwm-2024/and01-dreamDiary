package com.boostcamp.dreamteam.dreamdiary.core.model

import java.time.Instant

data class Diary(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val sleepStartAt: Instant,
    val sleepEndAt: Instant,
    val images: List<String>,
    val labels: List<Label>,
    val diaryContents: List<DiaryContent>,
)
