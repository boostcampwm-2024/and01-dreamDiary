package com.boostcamp.dreamteam.dreamdiary.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryLabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.util.UUID

@Dao
interface DreamDiaryDao {
    @Insert
    suspend fun insertDreamDiary(dreamDiaryEntity: DreamDiaryEntity)

    @Transaction
    suspend fun insertDreamDiary(
        title: String,
        body: String,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    ) {
        val dreamDiaryId = UUID.randomUUID().toString()
        insertDreamDiary(
            DreamDiaryEntity(
                id = dreamDiaryId,
                title = title,
                body = body,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                sleepStartAt = sleepStartAt,
                sleepEndAt = sleepEndAt,
            ),
        )
        setLabelsToDreamDiary(dreamDiaryId, labels)
    }

    @Insert
    suspend fun insertLabel(labelEntity: LabelEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabels(labelEntities: List<LabelEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDreamDiaryLabels(dreamDiaryEntity: List<DreamDiaryLabelEntity>)

    @Transaction
    suspend fun setLabelsToDreamDiary(
        diaryId: String,
        labels: List<String>,
    ) {
        insertLabels(labels.map { LabelEntity(it) })
        insertDreamDiaryLabels(labels.map { DreamDiaryLabelEntity(diaryId, it) })
    }

    @Query("select * from label where :search is null or label like :search")
    fun getLabels(search: String?): Flow<List<LabelEntity>>

    @Query("select * from diary order by updatedAt desc")
    fun getDreamDiaries(): PagingSource<Int, DreamDiaryEntity>
}
