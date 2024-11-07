package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "label",
)
data class LabelEntity(
    @PrimaryKey
    val label: String,
)
