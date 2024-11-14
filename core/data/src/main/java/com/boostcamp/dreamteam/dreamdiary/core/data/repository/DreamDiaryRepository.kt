package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface DreamDiaryRepository {
    suspend fun addDreamDiary(
        title: String,
        body: String,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    )

    suspend fun addLabel(label: String)

    fun getLabels(search: String): Flow<List<Label>>

    suspend fun addDreamDiaryLabel(
        diaryId: String,
        labels: List<String>,
    )
}
