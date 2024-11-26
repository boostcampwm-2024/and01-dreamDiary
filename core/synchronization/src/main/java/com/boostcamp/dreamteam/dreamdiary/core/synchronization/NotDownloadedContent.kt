package com.boostcamp.dreamteam.dreamdiary.core.synchronization

sealed class NotDownloadedContent {
    data class Text(val id: String, val diaryId: String) : NotDownloadedContent()
    data class Image(val id: String, val diaryId: String) : NotDownloadedContent()
}
