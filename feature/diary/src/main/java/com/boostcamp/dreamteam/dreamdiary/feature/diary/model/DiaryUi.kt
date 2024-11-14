package com.boostcamp.dreamteam.dreamdiary.feature.diary.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.vo.toDisplayableDateTime
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime

data class DiaryUi(
    val id: String,
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

internal fun Diary.toDiaryUi(): DiaryUi =
    run {
        DiaryUi(
            id = id,
            title = title,
            content = content,
            createdAt = createdAt.toDisplayableDateTime(),
            updatedAt = updatedAt.toDisplayableDateTime(),
            images = images,
            labels = labels.map { it.toLabelUi() },
            sleepStartAt = sleepStartAt.toDisplayableDateTime(),
            sleepEndAt = sleepEndAt.toDisplayableDateTime(),
            // TODO 어떤 값으로 정렬할 것인지 선택이 가능
            sortKey = sleepEndAt.toDisplayableDateTime(),
        )
    }

internal val diaryPreview1 = Diary(
    id = "1",
    title = "오늘의 일기",
    content = "오늘은 날씨가 좋았다.",
    createdAt = LocalDate.of(2021, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    updatedAt = LocalDate.of(2021, 9, 2).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    images = emptyList(),
    labels = listOf(
        Label("기쁨"),
        Label("행복"),
        Label("환희"),
    ),
    sleepStartAt = LocalDate.of(2021, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    sleepEndAt = LocalDate.of(2021, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
).toDiaryUi()

internal val diaryPreview2 = Diary(
    id = "2",
    title = "어제의 일기",
    content = "어제는 날씨가 좋지 않았다.",
    createdAt = LocalDate.of(2021, 8, 30).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    updatedAt = LocalDate.of(2021, 8, 31).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    images = emptyList(),
    labels = listOf(
        Label("슬픔"),
        Label("우울"),
    ),
    sleepStartAt = LocalDate.of(2021, 8, 30).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    sleepEndAt = LocalDate.of(2021, 8, 31).atStartOfDay(ZoneId.systemDefault()).toInstant(),
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
        )
        DiaryUi(
            id = it.toString(),
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
