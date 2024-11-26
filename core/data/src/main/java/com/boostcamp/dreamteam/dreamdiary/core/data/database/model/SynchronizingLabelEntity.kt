package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "synchronizing_label",
    foreignKeys = [
        ForeignKey(
            entity = SynchronizingDreamDiaryEntity::class,
            parentColumns = ["id"],
            childColumns = ["diaryId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("diaryId")],
)
data class SynchronizingLabelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val diaryId: String,
)

data class InsertSynchronizingLabel(
    val name: String,
    val diaryId: String,
)
