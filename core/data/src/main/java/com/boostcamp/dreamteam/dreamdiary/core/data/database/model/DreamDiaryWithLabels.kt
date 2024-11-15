package com.boostcamp.dreamteam.dreamdiary.core.data.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DreamDiaryWithLabels(
    @Embedded val dreamDiary: DreamDiaryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "label",
        associateBy = Junction(
            value = DreamDiaryLabelEntity::class,
            parentColumn = "diaryId",
            entityColumn = "labelId",
        ),
    )
    val labels: List<LabelEntity>,
)
