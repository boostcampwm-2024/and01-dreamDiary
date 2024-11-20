package com.boostcamp.dreamteam.dreamdiary.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryLabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryWithLabels
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.ImageEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.TextEntity
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

    @Query(
        """
        update diary
        set title = :title, body = :body, sleepStartAt = :sleepStartAt, sleepEndAt = :sleepEndAt, updatedAt = :updatedAt
        where id = :diaryId
        """,
    )
    suspend fun updateDreamDiary(
        diaryId: String,
        title: String,
        body: String,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
        updatedAt: Instant = Instant.now(),
    )

    @Transaction
    suspend fun updateDreamDiary(
        diaryId: String,
        title: String,
        body: String,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
        updatedAt: Instant = Instant.now(),
    ) {
        updateDreamDiary(
            diaryId = diaryId,
            title = title,
            body = body,
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
        )

        deleteDreamDiaryLabels(diaryId = diaryId)
        setLabelsToDreamDiary(
            diaryId = diaryId,
            labels = labels,
        )
    }

    @Insert
    suspend fun insertText(textEntity: TextEntity)

    @Query("select * from text where :id = id")
    suspend fun getText(id: String): TextEntity?

    @Insert
    suspend fun insertImage(imageEntity: ImageEntity)

    @Query("select * from image where :id = id")
    suspend fun getImage(id: String): ImageEntity?

    @Insert
    suspend fun insertLabel(labelEntity: LabelEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabels(labelEntities: List<LabelEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDreamDiaryLabels(dreamDiaryEntity: List<DreamDiaryLabelEntity>)

    @Query("delete from diary_label where diaryId = :diaryId")
    suspend fun deleteDreamDiaryLabels(diaryId: String)

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
    fun getDreamDiaries(): PagingSource<Int, DreamDiaryWithLabels>

    @Query(
        "select Distinct diary.* from diary join diary_label on diary.id = diary_label.diaryId join label on label.label = diary_label.labelId where label.label in (:labels) order by updatedAt desc",
    )
    fun getDreamDiariesByLabels(labels: List<String>): PagingSource<Int, DreamDiaryWithLabels>

    @Query("SELECT * FROM diary WHERE sleepEndAt BETWEEN :start AND :end")
    fun getDreamDiariesBySleepEndInRange(
        start: Instant,
        end: Instant,
    ): Flow<List<DreamDiaryEntity>>

    @Transaction
    @Query("select * from diary where id = :id")
    suspend fun getDreamDiary(id: String): DreamDiaryWithLabels

    @Transaction
    @Query("select * from diary where id = :id")
    fun getDreamDiaryAsFlow(id: String): Flow<DreamDiaryWithLabels>
}
