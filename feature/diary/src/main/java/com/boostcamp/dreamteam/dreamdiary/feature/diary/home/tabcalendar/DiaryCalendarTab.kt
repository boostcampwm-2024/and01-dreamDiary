package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component.DiaryCalendar
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diariesPreview
import java.time.YearMonth

@Composable
internal fun DiaryCalendarTab(
    onYearMothChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    state: DiaryHomeTabCalendarUIState = DiaryHomeTabCalendarUIState(),
) {
    Surface(modifier = modifier) {
        DiaryCalendar(
            diariesOfMonth = state.diariesOfMonth,
            modifier = Modifier.padding(horizontal = 16.dp),
            yearMonth = state.yearMonth,
            onYearMothChange = onYearMothChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarTabPreview() {
    DreamdiaryTheme {
        DiaryCalendarTab(
            onYearMothChange = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

internal val diaryHomeTabCalendarUIStatePreview = DiaryHomeTabCalendarUIState(
    yearMonth = YearMonth.now(),
    diariesOfMonth = diariesPreview,
)
