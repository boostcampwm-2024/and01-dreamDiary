package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "synchronizing_content",
    indices = [Index("diaryId")],
)
data class SynchronizingContentEntity(
    @PrimaryKey
    val id: String,
    val diaryId: String,
    val needUpload: Boolean,
    val isDone: Boolean,
    val type: String,
)
