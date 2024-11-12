package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "diary_label",
    primaryKeys = ["diaryId", "labelId"],
    foreignKeys = [
        ForeignKey(entity = DreamDiaryEntity::class, parentColumns = ["id"], childColumns = ["diaryId"]),
        ForeignKey(entity = LabelEntity::class, parentColumns = ["label"], childColumns = ["labelId"]),
    ],
)
data class DreamDiaryLabelEntity(
    val diaryId: String,
    val labelId: String,
)
