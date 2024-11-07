package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

interface DreamDiaryRepository {
    suspend fun addDreamDiary(
        title: String,
        body: String,
    )

    fun getDreamDiaries(): Flow<PagingData<Diary>>

    suspend fun addLabel(label: String)

    fun getLabels(search: String): Flow<List<Label>>
}
