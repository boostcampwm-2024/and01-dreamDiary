package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.Label

@Composable
internal fun DiaryCard(
    diary: Diary,
    modifier: Modifier = Modifier,
    onDiaryClick: (Diary) -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
) {
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
                Text(text = diary.createdAt, style = MaterialTheme.typography.labelMedium)

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
    // TODO: 테마 적용
    DiaryCard(
        diary = diaryPreview1,
        modifier = Modifier.fillMaxWidth(),
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
)

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
)