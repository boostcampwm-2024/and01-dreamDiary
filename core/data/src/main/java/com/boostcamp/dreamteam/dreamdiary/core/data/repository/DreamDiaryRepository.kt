package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import kotlinx.coroutines.flow.Flow

interface DreamDiaryRepository {
    suspend fun addDreamDiary(
        title: String,
        body: String,
        labels: List<String>,
    )

    suspend fun addLabel(label: String)

    fun getLabels(search: String): Flow<List<Label>>

    suspend fun addDreamDiaryLabel(
        diaryId: String,
        labels: List<String>,
    )
}
