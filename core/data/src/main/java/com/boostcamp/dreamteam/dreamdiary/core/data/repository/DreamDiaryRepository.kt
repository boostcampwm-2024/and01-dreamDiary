package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import kotlinx.coroutines.flow.Flow

interface DreamDiaryRepository {
    suspend fun addDreamDiary(
        title: String,
        body: String,
    )

    suspend fun addLabel(label: String)

    fun getLabels(): Flow<List<Label>>
}
