package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components.DiaryCalendar

@Composable
internal fun DiaryCalendarTab(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        DiaryCalendar(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarTabPreview() {
    DreamdiaryTheme {
        DiaryCalendarTab()
    }
}