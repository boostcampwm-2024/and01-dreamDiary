package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.Instant

@Entity(
    tableName = "synchronizing_diary",
)
data class SynchronizingDreamDiaryEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val body: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val sleepStartAt: Instant,
    val sleepEndAt: Instant,
    val version: String,
    val needData: Boolean,
)

data class SynchronizingDreamDiaryWithLabels(
    @Embedded val diary: SynchronizingDreamDiaryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "diaryId",
    )
    val labels: List<SynchronizingLabelEntity>,
)
