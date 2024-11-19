package com.boostcamp.dreamteam.dreamdiary.core.model

sealed class DiaryContent {
    class Text(val id: String, val text: String) : DiaryContent()

    class Image(val id: String, val path: String) : DiaryContent()
}
