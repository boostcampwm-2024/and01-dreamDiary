package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdCard
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview1
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

@Composable
internal fun DiaryCard(
    diary: DiaryUi,
    onDiaryClick: (DiaryUi) -> Unit,
    onDeleteDiary: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    DdCard(
        headline = {
            CardHeadline(
                diary = diary,
                onDeleteDiary = onDeleteDiary,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = modifier.clickable(onClick = { onDiaryClick(diary) }),
        overline = {
            CardOverline(
                diary = diary,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
            )
        },
        underline = {
            CardUnderline(
                diary = diary,
            )
        },
        tail = {
            CardTail(diary)
        },
    ) {
        CardBody(diary)
    }
}

@Composable
private fun CardBody(
    diary: DiaryUi,
    modifier: Modifier = Modifier,
) {
    Text(
        text = diary.textContent,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun CardOverline(
    diary: DiaryUi,
    modifier: Modifier = Modifier,
) {
    diary.images.firstOrNull()?.let {
        SubcomposeAsyncImage(
            model = it,
            contentDescription = stringResource(R.string.home_list_card_thumbnail, diary.title),
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    }
}

@Composable
private fun CardHeadline(
    diary: DiaryUi,
    onDeleteDiary: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = diary.title,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        IconButton(onClick = { isMenuVisible = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.home_list_card_menu),
                modifier = Modifier.height(16.dp),
            )
            DropdownMenu(
                expanded = isMenuVisible,
                onDismissRequest = { isMenuVisible = false },
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.home_list_card_menu_delete)) },
                    onClick = {
                        onDeleteDiary(diary)
                        isMenuVisible = false
                    },
                )
            }
        }
    }
}

@Composable
private fun CardUnderline(
    diary: DiaryUi,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.getDefault(),
) {
    val dateFormatter = remember(locale) {
        val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.FULL,
            null,
            Chronology.ofLocale(locale),
            locale,
        )
        DateTimeFormatter.ofPattern(pattern)
    }

    val timeFormatter = remember(locale) {
        val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            null,
            FormatStyle.SHORT,
            Chronology.ofLocale(locale),
            locale,
        )
        DateTimeFormatter.ofPattern(pattern)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = stringResource(id = R.string.home_list_card_create_date),
            modifier = Modifier
                .height(16.dp)
                .padding(2.dp),
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = diary.createdAt.value.format(dateFormatter),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = stringResource(id = R.string.home_list_card_create_time),
            modifier = Modifier
                .height(16.dp)
                .padding(2.dp),
        )
        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = diary.createdAt.value.format(timeFormatter),
        )
    }
}

@Composable
private fun CardTail(
    diary: DiaryUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Label,
            contentDescription = stringResource(R.string.home_list_card_label),
            modifier = Modifier.height(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))

        if (diary.labels.isNotEmpty()) {
            Text(text = diary.labels.joinToString { it.name })
        } else {
            Text(
                text = stringResource(R.string.home_list_card_label_empty),
                color = MaterialTheme.colorScheme.secondary,
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
            onDiaryClick = { },
            onDeleteDiary = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
