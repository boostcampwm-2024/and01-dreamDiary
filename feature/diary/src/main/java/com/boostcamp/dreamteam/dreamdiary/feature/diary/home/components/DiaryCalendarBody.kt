package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                firstDateOfWeek = firstDayOfFirstWeek.plusWeeks(i.toLong()),
                modifier = Modifier.fillMaxWidth(),
            )
        }
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
            DayCell(modifier = Modifier.weight(1f)) {
                Text(
                    text = DayOfWeek.of(i).getDisplayName(
                        TextStyle.SHORT,
                        locale,
                    ),
                )
            }
        }
    }
}

@Composable
private fun WeekRow(
    firstDateOfWeek: LocalDate,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        for (i in 0 until 7) {
            DayCell(modifier = Modifier.weight(1f)) {
                Text(text = (firstDateOfWeek.plusDays(i.toLong()).dayOfMonth).toString())
            }
        }
    }
}

@Composable
private fun DayCell(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarBodyPreview() {
    DreamdiaryTheme {
        DiaryCalendarBody(yearMonth = YearMonth.now())
    }
}
