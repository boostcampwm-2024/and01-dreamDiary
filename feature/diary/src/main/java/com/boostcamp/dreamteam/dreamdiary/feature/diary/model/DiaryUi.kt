package com.boostcamp.dreamteam.dreamdiary.feature.diary.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.vo.toDisplayableDateTime
import java.time.Instant
import java.time.YearMonth
import java.time.ZonedDateTime

data class DiaryUi(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: DisplayableDateTime,
    val updatedAt: DisplayableDateTime,
    val images: List<String>,
    val labels: List<LabelUi>,
    val sleepStartAt: DisplayableDateTime,
    val sleepEndAt: DisplayableDateTime,
    val sortKey: DisplayableDateTime,
)

// FIXME: 시간과 관련된 데이터를 처리하는 로직은 수정 해주세요
internal fun Diary.toDiaryUi(): DiaryUi = run {
    val sleepEndAt = Instant.now().toDisplayableDateTime()
    DiaryUi(
        id = id,
        title = title,
        content = content,
        createdAt = Instant.now().toDisplayableDateTime(),
        updatedAt = Instant.now().toDisplayableDateTime(),
        images = images,
        labels = labels.map { it.toLabelUi() },
        sleepStartAt = Instant.now().toDisplayableDateTime(),
        sleepEndAt = sleepEndAt,
        sortKey = sleepEndAt,
    )
}

internal val diaryPreview1 = Diary(
    id = 1,
    title = "오늘의 일기",
    content = "오늘은 날씨가 좋았다.",
    createdAt = "2021-09-01",
    updatedAt = "2021-09-01",
    images = emptyList(),
    labels = listOf(
        Label("기쁨"),
        Label("행복"),
        Label("환희"),
    ),
).toDiaryUi()

internal val diaryPreview2 = Diary(
    id = 2,
    title = "어제의 일기",
    content = "어제는 날씨가 좋지 않았다.",
    createdAt = "2021-08-31",
    updatedAt = "2021-08-31",
    images = emptyList(),
    labels = listOf(
        Label("슬픔"),
        Label("우울"),
    ),
).toDiaryUi()

internal val diariesPreview = run {
    val firstDayOfMonth = YearMonth
        .now()
        .atDay(1)
        .atStartOfDay()
        .atZone(ZonedDateTime.now().zone)
    val daysToDisplay = listOf(1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
    daysToDisplay.map {
        val displayableDateTime = DisplayableDateTime(
            value = firstDayOfMonth.plusDays(it.toLong() - 1),
            formatted = "",
        )
        DiaryUi(
            id = it.toLong(),
            title = "오늘의 일기",
            content = "오늘은 날씨가 좋았다.",
            createdAt = displayableDateTime,
            updatedAt = displayableDateTime,
            images = emptyList(),
            labels = emptyList(),
            sleepStartAt = displayableDateTime,
            sleepEndAt = displayableDateTime,
            sortKey = displayableDateTime,
        )
    }
}
