package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.model.DiarySort
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

    /**
     * Add dream diary
     *
     * @return diary id when success
     */
    suspend fun addDreamDiary(
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    ): String

    suspend fun updateDreamDiary(
        diaryId: String,
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    )

    fun getDreamDiaries(): Flow<PagingData<Diary>>

    fun getDreamDiariesOrderBy(sort: DiarySort): Flow<PagingData<Diary>>

    fun getDreamDiariesByLabel(labels: List<String>): Flow<PagingData<Diary>>

    fun getDreamDiariesByLabelsOrderBy(
        labels: List<String>,
        sort: DiarySort,
    ): Flow<PagingData<Diary>>

    suspend fun addLabel(label: String)

    suspend fun deleteLabel(label: String)

    fun getLabels(search: String): Flow<List<Label>>

    suspend fun addDreamDiaryLabel(
        diaryId: String,
        labels: List<String>,
    )

    suspend fun getDreamDiariesForToday(
        start: Instant,
        end: Instant,
    ): List<Diary>

    fun getDreamDiariesBySleepEndInRange(
        start: Instant,
        end: Instant,
    ): Flow<List<Diary>>

    suspend fun getDreamDiary(id: String): Diary

    fun getDreamDiaryAsFlow(id: String): Flow<Diary>

    suspend fun deleteDreamDiary(diaryId: String)
}
