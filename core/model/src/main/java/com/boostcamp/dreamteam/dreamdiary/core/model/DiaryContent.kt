package com.boostcamp.dreamteam.dreamdiary.core.model

sealed class DiaryContent {
    class Text(val text: String): DiaryContent()
    class Image(val path: String): DiaryContent()
}
