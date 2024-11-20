package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
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

    suspend fun addDreamDiary(
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    )

    suspend fun updateDreamDiary(
        diaryId: String,
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String>,
        sleepStartAt: Instant?,
        sleepEndAt: Instant?,
    )

    fun getDreamDiaries(): Flow<PagingData<Diary>>

    fun getDreamDiariesByLabel(labels: List<String>): Flow<PagingData<Diary>>

    suspend fun addLabel(label: String)

    fun getLabels(search: String): Flow<List<Label>>

    suspend fun addDreamDiaryLabel(
        diaryId: String,
        labels: List<String>,
    )

    fun getDreamDiariesBySleepEndInRange(
        start: Instant,
        end: Instant,
    ): Flow<List<Diary>>

    suspend fun getDreamDiary(id: String): Diary
}
