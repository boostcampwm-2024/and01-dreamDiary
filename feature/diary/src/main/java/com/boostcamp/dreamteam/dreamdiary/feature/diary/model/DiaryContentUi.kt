package com.boostcamp.dreamteam.dreamdiary.feature.diary.model

import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent

sealed class DiaryContentUi {
    data class Text(val text: String) : DiaryContentUi()

    data class Image(val path: String) : DiaryContentUi()
}

fun DiaryContentUi.toDomain(): DiaryContent {
    return when (this) {
        is DiaryContentUi.Text -> {
            DiaryContent.Text(
                text = this.text,
            )
        }

        is DiaryContentUi.Image -> {
            DiaryContent.Image(
                path = this.path,
            )
        }
    }
}
