package com.boostcamp.dreamteam.dreamdiary.community.model

import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import java.time.Instant
import java.time.ZoneId

data class DiaryUi(
    val id: String,
    val thumbnail: String? = null,
    val title: String,
    val previewText: String,
    val sharedAt: DisplayableDateTime,
    val commentCount: Long,
    val isLiked: Boolean,
    val author: UserUi,
)

internal val diaryUiPreview1 = DiaryUi(
    id = "1",
    title = "Diary 1",
    previewText = "This is a preview text for Diary 1",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 10,
    isLiked = false,
    author = userUiPreview1,
)

internal val diaryUiPreview2 = DiaryUi(
    id = "2",
    thumbnail = "https://picsum.photos/200/300",
    title = "Diary 2",
    previewText = "This is a preview text for Diary 2",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 2147483647,
    isLiked = true,
    author = userUiPreview2,
)

private val diaryUiPreview3 = DiaryUi(
    id = "3",
    title = "Diary 3",
    previewText = "This is a preview text for Diary 3",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 0,
    isLiked = false,
    author = userUiPreview3,
)

internal val diariesUiPreview = listOf(
    diaryUiPreview1,
    diaryUiPreview2,
    diaryUiPreview3,
)
