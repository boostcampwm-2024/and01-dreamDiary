package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "diary",
)
data class DreamDiaryEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val body: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant? = null,
    val sleepStartAt: Instant,
    val sleepEndAt: Instant,
)
