package com.boostcamp.dreamteam.dreamdiary.feature.diary.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Label

data class LabelUi(
    val name: String,
)

internal fun Label.toLabelUi() =
    LabelUi(
        name = name,
    )

internal val filteredLabelsPreview = listOf(
    LabelUi("악몽"),
    LabelUi("개꿈"),
    LabelUi("귀신"),
)

internal val selectedLabelsPreview = setOf(
    LabelUi("악몽"),
    LabelUi("공룡"),
)
