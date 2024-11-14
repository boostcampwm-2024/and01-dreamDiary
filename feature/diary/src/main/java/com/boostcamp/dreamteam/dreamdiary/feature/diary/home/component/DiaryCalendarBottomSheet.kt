package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryCalendarBottomSheet(
    diariesOfDay: List<DiaryUi>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = bottomSheetState
    ) {
    }
}

@Preview
@Composable
private fun DiaryCalendarBottomSheetPreview() {
    DreamdiaryTheme {
        DiaryCalendarBottomSheet(
            diariesOfDay = listOf(diaryPreview1),
            onDismissRequest = { /* no-op */ },
        )
    }
}
