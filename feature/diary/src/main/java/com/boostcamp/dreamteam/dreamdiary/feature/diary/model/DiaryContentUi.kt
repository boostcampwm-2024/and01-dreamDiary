package com.boostcamp.dreamteam.dreamdiary.feature.diary.model

import com.boostcamp.dreamteam.dreamdiary.core.model.NewDiaryContent

sealed class DiaryContentUi {
    data class Text(val text: String) : DiaryContentUi()

    data class Image(val path: String) : DiaryContentUi()
}

fun DiaryContentUi.toDomain(): NewDiaryContent {
    return when (this) {
        is DiaryContentUi.Text -> {
            NewDiaryContent.Text(
                text = this.text,
            )
        }

        is DiaryContentUi.Image -> {
            NewDiaryContent.Image(
                path = this.path,
            )
        }
    }
}
