package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun DiaryCalendarTab(modifier: Modifier = Modifier) {
    DiaryCalendar(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
private fun DiaryCalendarTabPreview() {
    DreamdiaryTheme {
        DiaryCalendarTab()
    }
}
