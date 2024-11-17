package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "image"
)
data class ImageEntity(
    @PrimaryKey
    val id: String,
    val path: String
)
