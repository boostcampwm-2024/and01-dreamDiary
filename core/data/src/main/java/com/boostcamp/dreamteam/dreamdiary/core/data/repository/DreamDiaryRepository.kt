package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow

interface DreamDiaryRepository {
    suspend fun addDreamDiary(
        title: String,
        body: String,
    )

    fun getDreamDiaries(): Flow<PagingData<Diary>>
}
