package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "text"
)
data class TextEntity(
    @PrimaryKey
    val id: String,
    val text: String,
)
