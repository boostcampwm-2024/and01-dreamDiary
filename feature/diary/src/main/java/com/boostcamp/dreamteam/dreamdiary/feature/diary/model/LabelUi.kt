package com.boostcamp.dreamteam.dreamdiary.feature.diary.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Label

data class LabelUi(
    val name: String,
)

internal fun Label.toLabelUi() =
    LabelUi(
        name = name,
    )
