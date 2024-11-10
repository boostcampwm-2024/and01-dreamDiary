package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
internal fun DiaryCalendarBody(
    yearMonth: YearMonth,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.getDefault(),
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
    val firstDayOfFirstWeek = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    val weeksInMonth = ChronoUnit.WEEKS.between(firstDayOfFirstWeek, lastDayOfMonth) + 1

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier,
    ) {
        WeekHeader(
            modifier = Modifier.fillMaxWidth(),
        )
        for (i in 0 until weeksInMonth.toInt()) {
            HorizontalDivider()
            WeekRow(
                currentYearMonth = yearMonth,
                firstDateOfWeek = firstDayOfFirstWeek.plusWeeks(i.toLong()),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun WeekHeader(
    modifier: Modifier = Modifier,
    locale: Locale = Locale.getDefault(),
) {
    val reorderedDays = (0 until 7).map { (it + 6) % 7 + 1 }

    Row(modifier = modifier) {
        reorderedDays.forEach { i ->
            DayCell(
                modifier = Modifier.weight(1f),
                minHeight = 24.dp,
            ) {
                Text(
                    text = DayOfWeek.of(i).getDisplayName(
                        TextStyle.SHORT,
                        locale,
                    ),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun WeekRow(
    currentYearMonth: YearMonth,
    firstDateOfWeek: LocalDate,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        for (i in 0 until 7) {
            DayCell(
                modifier = Modifier.weight(1f),
            ) {
                val day = firstDateOfWeek.plusDays(i.toLong())
                Text(
                    text = (day.dayOfMonth).toString(),
                    color = if (day.month != currentYearMonth.month) {
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    modifier: Modifier = Modifier,
    minHeight: Dp = 48.dp,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier.defaultMinSize(minHeight = minHeight),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarBodyPreview() {
    DreamdiaryTheme {
        DiaryCalendarBody(yearMonth = YearMonth.of(2024, 1))
    }
}
