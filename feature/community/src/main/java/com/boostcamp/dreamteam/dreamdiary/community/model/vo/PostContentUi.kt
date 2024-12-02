package com.boostcamp.dreamteam.dreamdiary.community.model.vo

import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent

sealed class PostContentUi {
    data class Text(
        val text: String,
    ) : PostContentUi()

    data class Image(
        val path: String,
    ) : PostContentUi()
}

internal fun DiaryContent.toPostContentUi(): PostContentUi =
    when (this) {
        is DiaryContent.Text -> PostContentUi.Text(text = text)
        is DiaryContent.Image -> PostContentUi.Image(path = path)
    }

fun PostContentUi.toDomain(): DiaryContent {
    return when (this) {
        is PostContentUi.Text -> {
            DiaryContent.Text(
                text = this.text,
            )
        }

        is PostContentUi.Image -> {
            DiaryContent.Image(
                path = this.path,
            )
        }
    }
}
