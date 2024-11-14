package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component.DiaryCalendar
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component.DiaryCalendarBottomSheet
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diariesPreview
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryCalendarTab(
    onDiaryClick: (DiaryUi) -> Unit,
    onYearMothChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    state: DiaryHomeTabCalendarUIState = DiaryHomeTabCalendarUIState(),
) {
    var lastSelectedDay: LocalDate by rememberSaveable {
        mutableStateOf(
            ZonedDateTime.now().toLocalDate(),
        )
    }
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }

    Surface(modifier = modifier) {
        DiaryCalendar(
            diariesOfMonth = state.diariesOfMonth,
            modifier = Modifier.padding(horizontal = 16.dp),
            yearMonth = state.yearMonth,
            onYearMothChange = onYearMothChange,
            onDayClick = {
                lastSelectedDay = it
                isBottomSheetOpen = true
            },
        )

        if (isBottomSheetOpen) {
            DiaryCalendarBottomSheet(
                diariesOfDay = state.diariesOfMonth.filter {
                    it.sortKey.value
                        .toLocalDate()
                        .isEqual(lastSelectedDay)
                },
                onDiaryClick = onDiaryClick,
                onDismissRequest = { isBottomSheetOpen = false },
                onBackClick = { lastSelectedDay = lastSelectedDay.minusDays(1) },
                onForwardClick = { lastSelectedDay = lastSelectedDay.plusDays(1) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarTabPreview() {
    DreamdiaryTheme {
        DiaryCalendarTab(
            onDiaryClick = { /* no-op */ },
            onYearMothChange = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

internal val diaryHomeTabCalendarUIStatePreview = DiaryHomeTabCalendarUIState(
    yearMonth = YearMonth.now(),
    diariesOfMonth = diariesPreview,
)
