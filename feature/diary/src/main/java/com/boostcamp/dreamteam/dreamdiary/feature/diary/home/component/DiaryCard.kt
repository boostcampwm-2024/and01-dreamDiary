package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview1
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.vo.toDisplayableDateTime
import java.time.Duration
import java.time.Instant
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

@Composable
internal fun DiaryCard(
    diary: DiaryUi,
    modifier: Modifier = Modifier,
    onDiaryClick: (DiaryUi) -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
) {
    val locale = Locale.current.platformLocale
    val dateFormatter = remember(locale) {
        val pattern = DateTimeFormatterBuilder
            .getLocalizedDateTimePattern(
                FormatStyle.FULL,
                null,
                Chronology.ofLocale(locale),
                locale,
            )
        DateTimeFormatter.ofPattern(pattern)
    }

    val timeFormatter = remember(locale) {
        val pattern = DateTimeFormatterBuilder
            .getLocalizedDateTimePattern(
                null,
                FormatStyle.SHORT,
                Chronology.ofLocale(locale),
                locale,
            )
        DateTimeFormatter.ofPattern(pattern)
    }

    Card(
        modifier = modifier.clickable(onClick = { onDiaryClick(diary) }),
        colors = CardDefaults.cardColors().copy(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            diary.images.firstOrNull()?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "${diary.title} 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f),
                )
            }
            Text(
                text = diary.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            MaterialTheme.colorScheme.secondary
            Row(verticalAlignment = Alignment.Bottom) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "작성일",
                    modifier = Modifier
                        .height(16.dp)
                        .padding(2.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = diary.updatedAt.value.format(dateFormatter), style = MaterialTheme.typography.labelMedium)

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "작성시간",
                    modifier = Modifier
                        .height(16.dp)
                        .padding(2.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = diary.updatedAt.value.format(timeFormatter), style = MaterialTheme.typography.labelMedium)

                Spacer(modifier = Modifier.width(8.dp))

                if (diary.labels.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Label,
                        contentDescription = "라벨",
                        modifier = Modifier.height(16.dp),
                    )
                    diary.labels.joinToString { it.name }.let {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = it, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = diary.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun DiaryCardPreview() {
    DreamdiaryTheme {
        DiaryCard(
            diary = diaryPreview1,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

internal val diaryPreview1 = DiaryUi(
    id = "1",
    title = "오늘의 일기",
    content = "오늘은 날씨가 좋았다",
    createdAt = Instant.now().toDisplayableDateTime(),
    updatedAt = Instant.now().toDisplayableDateTime(),
    sleepStartAt = Instant.now().toDisplayableDateTime(),
    sleepEndAt = Instant.now().toDisplayableDateTime(),
    images = listOf(),
    labels = listOf(
        LabelUi("기쁨"),
        LabelUi("행복"),
        LabelUi("환희"),
    ),
)

internal val diaryPreview2 = DiaryUi(
    id = "2",
    title = "어제의 일기",
    content = "어제는 날씨가 좋지 않았다.",
    createdAt = Instant.now().minus(Duration.ofDays(1)).toDisplayableDateTime(),
    updatedAt = Instant.now().minus(Duration.ofDays(1)).toDisplayableDateTime(),
    sleepStartAt = Instant.now().toDisplayableDateTime(),
    sleepEndAt = Instant.now().toDisplayableDateTime(),
    images = listOf(),
    labels = listOf(
        LabelUi("슬픔"),
        LabelUi("우울"),
    ),
)
