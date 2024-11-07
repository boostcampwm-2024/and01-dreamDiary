package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

fun LabelEntity.toDomain(): Label {
    return Label(
        name = this.label
    )
}
