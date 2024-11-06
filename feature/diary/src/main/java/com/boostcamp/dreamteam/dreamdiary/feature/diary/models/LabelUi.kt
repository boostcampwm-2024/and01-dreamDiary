package com.boostcamp.dreamteam.dreamdiary.feature.diary.models

import com.boostcamp.dreamteam.dreamdiary.core.model.Label

data class LabelUi(
    val name: String,
)

internal fun Label.toLabelUi() =
    LabelUi(
        name = name,
    )
