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
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.InsertSynchronizingLabel
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingContentEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingDreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingDreamDiaryWithLabels
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingLabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.TextEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.synchronization.DreamDiaryOnlyVersion
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.util.UUID

@Dao
interface DreamDiaryDao {
    @Insert
    suspend fun insertDreamDiary(dreamDiaryEntity: DreamDiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDreamDiaryReplace(dreamDiaryEntity: DreamDiaryEntity)

    @Transaction
    suspend fun insertDreamDiary(
        title: String,
        body: String,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    ): String {
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
                needSync = true,
                lastSyncVersion = "init",
                currentVersion = UUID.randomUUID().toString(),
            ),
        )
        setLabelsToDreamDiary(dreamDiaryId, labels)

        return dreamDiaryId
    }

    @Query(
        """
            update diary
            set title = :title, body = :body, sleepStartAt = :sleepStartAt, sleepEndAt = :sleepEndAt, updatedAt = :updatedAt, currentVersion = :currentVersion
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
        currentVersion: String = UUID.randomUUID().toString(),
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

    @Transaction
    suspend fun deleteDreamDiary(diaryId: String) {
        deleteDreamDiaryLabels(diaryId = diaryId)
        privateDeleteDreamDiary(diaryId = diaryId)
    }

    @Query("update diary set deletedAt = :deletedAt, currentVersion = :currentVersion where id = :diaryId")
    suspend fun privateDeleteDreamDiary(
        diaryId: String,
        deletedAt: Instant = Instant.now(),
        currentVersion: String = UUID.randomUUID().toString(),
    )

    @Insert
    suspend fun insertText(textEntity: TextEntity)

    @Query("select * from text where :id = id")
    suspend fun getText(id: String): TextEntity?

    @Query("delete from text where id = :id")
    suspend fun deleteText(id: String)

    @Insert
    suspend fun insertImage(imageEntity: ImageEntity)

    @Query("select * from image where :id = id")
    suspend fun getImage(id: String): ImageEntity?

    @Query("delete from image where id = :id")
    suspend fun deleteImage(id: String)

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

    @Query("select * from diary where deletedAt is null order by updatedAt desc")
    fun getDreamDiaries(): PagingSource<Int, DreamDiaryWithLabels>

    @Query(
        """
            select Distinct diary.*
            from diary
                join diary_label on diary.id = diary_label.diaryId
                join label on label.label = diary_label.labelId
            where label.label in (:labels)
                and diary.deletedAt is null
            order by updatedAt desc
        """,
    )
    fun getDreamDiariesByLabels(labels: List<String>): PagingSource<Int, DreamDiaryWithLabels>

    @Query(
        """
            SELECT *
            FROM diary
            WHERE createdAt BETWEEN :start AND :end
                AND deletedAt IS NULL
        """,
    )
    suspend fun getDreamDiariesForToday(
        start: Instant,
        end: Instant,
    ): List<DreamDiaryEntity>

    @Query(
        """
            select *
            from diary
            where deletedAt is null
            order by
                case when :sortType = 'createdAt' and :orderCode = 0 then createdAt end asc,
                case when :sortType = 'createdAt' and :orderCode = 1 then createdAt end desc,
                case when :sortType = 'updatedAt' and :orderCode = 0 then updatedAt end asc,
                case when :sortType = 'updatedAt' and :orderCode = 1 then updatedAt end desc,
                case when :sortType = 'sleepEndAt' and :orderCode = 0 then sleepEndAt end asc,
                case when :sortType = 'sleepEndAt' and :orderCode = 1 then sleepEndAt end desc
            """,
    )
    fun getDreamDiariesOrderBy(
        sortType: String,
        orderCode: Int,
    ): PagingSource<Int, DreamDiaryWithLabels>

    @Query(
        """
            select *
            from diary
                join diary_label on diary.id = diary_label.diaryId
                join label on label.label = diary_label.labelId
            where label.label in (:labels)
                and deletedAt is null
            order by
                case when :sortType = 'createdAt' and :orderCode = 0 then createdAt end asc,
                case when :sortType = 'createdAt' and :orderCode = 1 then createdAt end desc,
                case when :sortType = 'updatedAt' and :orderCode = 0 then updatedAt end asc,
                case when :sortType = 'updatedAt' and :orderCode = 1 then updatedAt end desc,
                case when :sortType = 'sleepEndAt' and :orderCode = 0 then sleepEndAt end asc,
                case when :sortType = 'sleepEndAt' and :orderCode = 1 then sleepEndAt end desc
            """,
    )
    fun getDreamDiariesByLabelsOrderBy(
        labels: List<String>,
        sortType: String,
        orderCode: Int,
    ): PagingSource<Int, DreamDiaryWithLabels>

    @Query(
        """
            SELECT *
            FROM diary
            WHERE sleepEndAt BETWEEN :start AND :end
                AND deletedAt IS NULL
        """,
    )
    fun getDreamDiariesBySleepEndInRange(
        start: Instant,
        end: Instant,
    ): Flow<List<DreamDiaryEntity>>

    @Transaction
    @Query("select * from diary where id = :id and deletedAt is null")
    suspend fun getDreamDiary(id: String): DreamDiaryWithLabels

    @Transaction
    @Query("select * from diary where id = :id and deletedAt is null")
    fun getDreamDiaryAsFlow(id: String): Flow<DreamDiaryWithLabels>

    @Query("select id, currentVersion from diary")
    suspend fun getDreamDiaryVersion(): List<DreamDiaryOnlyVersion>

    @Query("select id, version as currentVersion from synchronizing_diary")
    suspend fun getDreamDiaryVersionInSynchronizing(): List<DreamDiaryOnlyVersion>

    @Query("update diary set needSync = 1 where id = :id")
    suspend fun setNeedSync(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSynchronizingDreamDiary(synchronizingDreamDiaryEntity: SynchronizingDreamDiaryEntity)

    suspend fun insertSynchronizingDreamDiary(
        id: String,
        version: String,
    ) {
        insertSynchronizingDreamDiary(
            SynchronizingDreamDiaryEntity(
                id = id,
                title = "",
                body = "",
                createdAt = Instant.ofEpochMilli(0),
                updatedAt = Instant.ofEpochMilli(0),
                sleepStartAt = Instant.ofEpochMilli(0),
                sleepEndAt = Instant.ofEpochMilli(0),
                version = version,
                needData = true,
            ),
        )
    }

    @Transaction
    @Query("select * from diary where needSync = 1 or lastSyncVersion != currentVersion")
    suspend fun getDreamDiaryNeedSync(): List<DreamDiaryWithLabels>

    @Transaction
    @Query("select * from synchronizing_diary where needData = 1")
    suspend fun getSynchronizingDreamDiaryNeedData(): List<SynchronizingDreamDiaryEntity>

    @Query("delete from synchronizing_diary where id = :id")
    suspend fun deleteSynchronizingDreamDiary(id: String): Int

    @Transaction
    suspend fun deleteDreamDiaryHard(diaryId: String) {
        deleteDreamDiaryLabels(diaryId = diaryId)
        privateDeleteDreamDiaryHard(diaryId)
        deleteSynchronizingDreamDiary(diaryId)
    }

    @Query("delete from diary where id = :id")
    suspend fun privateDeleteDreamDiaryHard(id: String)

    @Insert(entity = SynchronizingLabelEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSynchronizingLabel(insertLabel: InsertSynchronizingLabel)

    @Query("delete from synchronizing_label where diaryId = :diaryId")
    suspend fun deleteSynchronizingLabelOfDiaryId(diaryId: String)

    @Query("update diary set lastSyncVersion = :version, currentVersion = :version, needSync = 0 where id = :id")
    suspend fun updateDreamDiarySyncVersionAndCurrentVersion(
        id: String,
        version: String,
    )

    @Transaction
    suspend fun insertSynchronizingDreamDiaryAndUpdateVersion(
        id: String,
        title: String,
        body: String,
        labels: List<String>,
        createdAt: Instant,
        updatedAt: Instant,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
        version: String,
    ) {
        insertSynchronizingDreamDiary(
            SynchronizingDreamDiaryEntity(
                id = id,
                title = title,
                body = body,
                createdAt = createdAt,
                updatedAt = updatedAt,
                sleepStartAt = sleepStartAt,
                sleepEndAt = sleepEndAt,
                version = version,
                needData = false,
            ),
        )
        deleteSynchronizingLabelOfDiaryId(diaryId = id)
        for (label in labels) {
            insertSynchronizingLabel(
                InsertSynchronizingLabel(
                    name = label,
                    diaryId = id,
                ),
            )
        }
        updateDreamDiarySyncVersionAndCurrentVersion(id, version)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSynchronizingContent(content: SynchronizingContentEntity)

    @Query("select * from synchronizing_content where id = :id")
    suspend fun getSynchronizingContent(id: String): SynchronizingContentEntity?

    @Transaction
    suspend fun insertSynchronizingContentWhenNotDone(
        contentId: String,
        diaryId: String,
        type: String,
    ) {
        val synchronizingContent = getSynchronizingContent(contentId)
        if (synchronizingContent == null || !synchronizingContent.isDone) {
            insertSynchronizingContent(
                SynchronizingContentEntity(
                    id = contentId,
                    diaryId = diaryId,
                    needUpload = true,
                    isDone = false,
                    type = type,
                ),
            )
        }
    }

    @Query("select * from synchronizing_content where needUpload = 1 and isDone = 0")
    suspend fun getNeedSynchronizingContents(): List<SynchronizingContentEntity>

    @Query("update synchronizing_content set isDone = 1 where id = :id")
    suspend fun setSynchronizingContentDone(id: String)

    @Query(
        """
            delete from synchronizing_diary
            where id in (
                select synchronizing_diary.id
                from synchronizing_diary, diary
                where synchronizing_diary.id = diary.id and synchronizing_diary.version != diary.currentVersion
            )
        """,
    )
    suspend fun removeSynchronizingDreamDiaryIfVersionNotEquals()

    @Query("select id from synchronizing_diary")
    suspend fun getSynchronizingDreamDiaryIds(): List<String>

    @Transaction
    @Query("select * from synchronizing_diary where id = :id and needData = 0")
    suspend fun getSynchronizingDreamDiaryWithLabels(id: String): SynchronizingDreamDiaryWithLabels?

    @Transaction
    suspend fun moveToDreamDiaryIfSynced(id: String) {
        val dreamDiaryWithLabels = getSynchronizingDreamDiaryWithLabels(id) ?: return

        val synchronizingDreamDiary = dreamDiaryWithLabels.diary
        val labels = dreamDiaryWithLabels.labels.map { it.name }

        val body = synchronizingDreamDiary.body
        body.split(":")

        val parsingDiaryContent = body.split(":")
        var index = 0

        while (index < parsingDiaryContent.size) {
            if (parsingDiaryContent[index] == "text") {
                index += 1
                val contentId = parsingDiaryContent[index]
                val textEntity = getText(contentId)
                if (textEntity == null) {
                    return
                }
            } else if (parsingDiaryContent[index] == "image") {
                index += 1
                val contentId = parsingDiaryContent[index]
                val imageEntity = getImage(contentId)
                if (imageEntity == null) {
                    return
                }
            } else {
                index += 1
            }
        }

        insertDreamDiaryReplace(
            DreamDiaryEntity(
                id = synchronizingDreamDiary.id,
                title = synchronizingDreamDiary.title,
                body = synchronizingDreamDiary.body,
                createdAt = synchronizingDreamDiary.createdAt,
                updatedAt = synchronizingDreamDiary.updatedAt,
                sleepStartAt = synchronizingDreamDiary.sleepStartAt,
                sleepEndAt = synchronizingDreamDiary.sleepEndAt,
                needSync = false,
                lastSyncVersion = synchronizingDreamDiary.version,
                currentVersion = synchronizingDreamDiary.version,
            ),
        )
        deleteDreamDiaryLabels(synchronizingDreamDiary.id)
        setLabelsToDreamDiary(
            diaryId = synchronizingDreamDiary.id,
            labels = labels,
        )
        deleteSynchronizingDreamDiary(synchronizingDreamDiary.id)
    }

    @Query("select * from synchronizing_diary")
    suspend fun getSynchronizingDreamDiaries(): List<SynchronizingDreamDiaryEntity>

    @Transaction
    suspend fun removeSyncData() {
        deleteAllSynchronizingDreamDiary()
        deleteAllSynchronizingContent()
    }

    @Query("delete from synchronizing_diary")
    suspend fun deleteAllSynchronizingDreamDiary(): Int

    @Query("delete from synchronizing_content")
    suspend fun deleteAllSynchronizingContent(): Int
}
