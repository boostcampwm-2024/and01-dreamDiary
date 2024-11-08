package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun DiaryCalendar(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth = YearMonth.now(),
) {
    var selectedYearMonth by remember { mutableStateOf(yearMonth) }

    Column(modifier = modifier) {
        DiaryCalendarHeader(
            yearMonth = selectedYearMonth,
            onPreviousMonthClick = { selectedYearMonth = selectedYearMonth.minusMonths(1) },
            onNextMonthClick = { selectedYearMonth = selectedYearMonth.plusMonths(1) },
            onMonthTextClick = { /*TODO: 년 월을 선택하는 dialog 표시*/ },
        )

        DiaryCalendarBody(yearMonth = selectedYearMonth, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun DiaryCalendarHeader(
    yearMonth: YearMonth,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier = Modifier,
    onMonthTextClick: () -> Unit = { },
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPreviousMonthClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                contentDescription = stringResource(R.string.calendar_previous_month),
            )
        }
        Row(
            modifier = Modifier.clickable(onClick = onMonthTextClick),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = yearMonth.year.toString(),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        IconButton(onClick = onNextMonthClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = stringResource(R.string.calendar_move_next_month),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarPreview1() {
    DreamdiaryTheme {
        DiaryCalendar(yearMonth = YearMonth.of(2024, 1))
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarPreview2() {
    DreamdiaryTheme {
        DiaryCalendar(yearMonth = YearMonth.of(2024, 2))
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarPreview3() {
    DreamdiaryTheme {
        DiaryCalendar(yearMonth = YearMonth.of(2024, 3))
    }
}
